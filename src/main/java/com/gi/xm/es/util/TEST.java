package com.gi.xm.es.util;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zcy on 16-12-12.
 */
public class TEST{

    static ConcurrentLinkedQueue<String> queues = new ConcurrentLinkedQueue<String>();
    static AtomicBoolean isInsert = new AtomicBoolean(true);
    static final String HOST = "10.9.130.135";
    static final String clustername = "elasticsearch";
    static TransportClient client = null;
    private static final Logger LOG = LoggerFactory.getLogger(TEST.class);

    //连接es client
    static {
        try {
            Settings settings = Settings.builder()
                    .put("cluster.name", clustername).build();
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOST), 9300));
        } catch (UnknownHostException e) {
            LOG.error("连接 client 异常", e);
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        importProjects();
    }

    /**
     *  项目
     */
    public static void importProjects(){
        boolean isDelete = deleteIndexData("xm_project_a","project");
        if(isDelete){
            String projectSql = "select " +
                    "p.id as sid," +
                    "p.title," +
                    "p.description," +
                    "p.pic_big_xm as logo, " +
                    "p.industry_name as indudstryName ," +
                    "p.industry_sub_name as indudstrySubName," +
                    "p.newest_event_round as roundName," +
                    "p.create_date as createDate " +
                    "from edw2.dm_project p where id > ? and id <= ?";
            excuteThread("xm_project_a","project",projectSql);
        }
    }

    public static void excuteThread(String index,String type,String sql){
        createIndex(index,type);
        int rowcount = writeData(sql);
        System.out.println("总条数:"+rowcount+"条");
    }

    public static long  createIndex( final String index,  final String type){
        final ConcurrentHashMap<String, Boolean> hashMap = new ConcurrentHashMap();
        Long endTime = null;
        ExecutorService exe = Executors.newFixedThreadPool(50);
        //开多线程读队列的数据
        for(int t =0 ;t<2; t++){
            exe.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    hashMap.put(Thread.currentThread().getName(), Boolean.FALSE);
                    int currentCount = 0;
                    final BulkProcessor bulkProcessor = BulkProcessor.builder(
                            client,
                            new BulkProcessor.Listener() {
                                //批量成功后执行
                                public void afterBulk(long l, BulkRequest bulkRequest,
                                                      BulkResponse bulkResponse) {

                                    System.out.println(Thread.currentThread().getName()+"请求数量："+ bulkRequest.numberOfActions());
                                    if (bulkResponse.hasFailures()) {
                                        for (BulkItemResponse item :
                                                bulkResponse.getItems()) {
                                            if (item.isFailed()) {
                                                System.out.println("失败信息:--------" +
                                                        item.getFailureMessage());
                                            }
                                        }
                                    }
                                }

                                //批量提交之前执行
                                public void beforeBulk(long executionId,
                                                       BulkRequest request) {
                                }

                                //批量失败后执行
                                public void afterBulk(long executionId,
                                                      BulkRequest request,
                                                      Throwable failure) {
                                    System.out.println("happen fail = " +
                                            failure.getMessage() + " , cause = " + failure.getCause());
                                }
                            })
                            .setBulkActions(5000)
                            .setBulkSize(new ByteSizeValue(10, ByteSizeUnit.MB))
                            .setBackoffPolicy(
                                    BackoffPolicy.exponentialBackoff(
                                            TimeValue.timeValueMillis(100), 3))
                            .setConcurrentRequests(1)
                            .setFlushInterval(TimeValue.timeValueSeconds(5))
                            .build();

                    //读取队列里的数据
                    while(true){
                        if(!queues.isEmpty()){
                            String json = queues.poll();
                            if (json == null) continue;
                            bulkProcessor.add(new IndexRequest(index,type).source(json));
                            json = null;
                            currentCount++;
                        }
                        //队列为空,并且MySQL读取数据完毕
                        if (queues.isEmpty() && !isInsert.get()) {
                            bulkProcessor.flush();
                            hashMap.put(Thread.currentThread().getName(), Boolean.TRUE);
                            while (hashMap.values().contains(Boolean.FALSE)) {
                                try {
                                    Thread.currentThread().sleep(1 * 1000);
                                } catch (Exception e) {
                                    e.printStackTrace(System.out);
                                }
                            }
                            try {
                                //关闭,如有未提交完成的文档则等待完成，最多等待1秒钟
                                bulkProcessor.awaitClose(1, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println(Thread.currentThread().getName()+": break");
                            break;
                        }

                    }
                }
            }));
        }
//        exe.shutdown();
//        while (true) {
//            if (exe.isTerminated()) {
//                System.out.println("结束了: "+endTime);
//                endTime = System.currentTimeMillis();
//                break;
//            }
//        }
        return System.currentTimeMillis();
    }



    /**
     * 读取mysql 数据
     * @param sql 查询语句
     */
    public static int writeData(String sql){
        Long start = System.currentTimeMillis();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String columnName = null;
        String value = null;
        int limit = 10000;
        int from = 0;
        int to = limit;
        int total = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://10.9.130.142/edw2?characterEncoding=UTF-8&useOldAliasMetadataBehavior=true";
            conn = DriverManager.getConnection(url, "xmuser", "qcDKywE7Ka52");
            System.out.println("写入数据开始，成功连接MySQL SQL:" + sql);

            while(true){
                ps = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                ps.setInt(1, from);
                ps.setInt(2, to);
                ps.setFetchSize(limit);
                rs = ps.executeQuery();
                ResultSetMetaData data= rs.getMetaData();
                int colCount = data.getColumnCount();
                LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
                int perCount = 0;
                if(total >  limit*2000){
                    break;
                }
                while(rs.next()){
                    for(int i = 1; i<= colCount; i++){
                        columnName = data.getColumnName(i); //获取列名
                        value = rs.getString(i);
                        if (value != null && !"".equals(value.trim()) && value.trim().length() > 0) {
                            map.put(columnName,value);
                        }else{
                            map.put(columnName,null);
                        }
                    }
                    perCount = perCount+1;
                    if(map.size()>0){
                        queues.add(JSON.toJSONString(map));
                    }
                    if(perCount % 10000 == 0){
                        int number = queues.size();
                        int j = number/10000;
                        try{
                            Thread.sleep(j*1000);
                        }catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        int number2 = queues.size();
                        j = number2/10000;
                    }
                }
                total+=perCount;
                from+=limit;
                to +=limit;
                data = null;
                map = null;
                ps = null;
                System.out.println("total:"+total);
                System.out.println("from :"+from+" to:"+to);
            }
            isInsert = new AtomicBoolean(false);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("mysql 用时:"+(System.currentTimeMillis()-start)+" ms");
        return total;
    }

    private static boolean deleteIndexData(String index,String type) {
        long startTime = System.currentTimeMillis();
        try {
            Runtime.getRuntime().exec("curl -XDELETE "+HOST+":9200/"+index);
            Runtime.getRuntime().exec(ESEXEC.ADDINDEX.get(index));
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("删除"+index+"数据共用时：" + (endTime - startTime)+"ms");
        return true;
    }

}
