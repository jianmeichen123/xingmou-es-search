package com.gi.xm.es.index;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.util.HTMLFilter;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zcy on 16-12-12.
 * 创建项目索引
 */
public class InvestfirmsIndex {

    private ConcurrentLinkedQueue<String> queues ;
    private AtomicBoolean isInsert;

    static TransportClient client = null;
    private static final Logger LOG = LoggerFactory.getLogger(InvestfirmsIndex.class);

    private String INDEX = "ctdn_investfirms";
    private String TYPE = "investfirms";

    //连接es client
    public InvestfirmsIndex(String host, String clusterName) {
        queues = new ConcurrentLinkedQueue<String>();
        isInsert = new AtomicBoolean(true);
        try {
            Settings settings = Settings.builder()
                    .put("cluster.name", clusterName).build();
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), 9300));
        } catch (UnknownHostException e) {
            LOG.error("连接 client 异常", e);
            e.printStackTrace();
        }
    }


    /**
     *  项目
     */
    public  void importInvestfirms(){
        boolean isDelete = deleteIndexData();
        if(isDelete){
            String sql = "select " +
                    "code," +
                    "id,"+
                    "focusDomain,"+
                    "investStage,"+
                    "orgType,"+
                    "districtId,"+
                    "districtSubId,"+
                    "capitalType,"+
                    "currencyTitle,"+
                    "logo,"+
                    "orgName,"+
                    "investTotal,"+
                    "totalRatio,"+
                    "investAmountNum,"+
                    "investAmountStr,"+
                    "amountRatio,"+
                    "investProj,"+
                    "newestInvestDate "+
                    "from app.app_org_info";
            excuteThread(sql);
        }
    }


    public  void excuteThread(String sql){
        long startTime = System.currentTimeMillis();
        createIndex();
        int rowcount = writeData(sql);
        long endTime = System.currentTimeMillis();
        System.out.println(INDEX+"数据写入完毕");
        System.out.println("总用时:"+(endTime - startTime)+"ms");
        System.out.println("总条数:"+rowcount+"条");
        LOG.info("项目索引更新,总条数："+rowcount);
    }

    public  void  createIndex(){
        final long currentTime = System.currentTimeMillis();
        final ConcurrentHashMap<String, Boolean> hashMap = new ConcurrentHashMap();
        //开多线程读队列的数据
        for(int t =0 ;t<10; t++){
            new Thread(new Runnable() {
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
                                                LOG.error("批量插入索引失败:",item.getFailureMessage());
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
                                    LOG.error("批量插入索引失败: " +
                                            failure.getMessage() + " , cause = " + failure.getCause());
                                }
                            })
                            .setBulkActions(10000)
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
                            bulkProcessor.add(new IndexRequest(INDEX,TYPE).source(json));
                            currentCount++;
                        }
                        if (queues.isEmpty() && !isInsert.get()) {
                            bulkProcessor.flush();
                            hashMap.put(Thread.currentThread().getName(), Boolean.TRUE);
                            while (hashMap.values().contains(Boolean.FALSE)) {
                                try {
                                    Thread.currentThread().sleep(1 * 1000);
                                } catch (Exception e) {
                                    LOG.error("Thread.currentThread().sleep 异常", e);
                                }
                            }
                            try {
                                bulkProcessor.awaitClose(10, TimeUnit.MINUTES);
                            } catch (InterruptedException e) {
                                LOG.error("关闭 client 异常", e);
                            }
                            break;

                        }
                    }
                }
            }).start();
        }
    }



    /**
     * 读取mysql 数据
     * @param sql 查询语句
     */
    public  int writeData(String sql){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        String columnName = null;
        String value = null;
        ClassPathXmlApplicationContext applicationContext =  applicationContext = new ClassPathXmlApplicationContext("classpath:application.xml");
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://10.9.130.142/app?characterEncoding=UTF-8&useOldAliasMetadataBehavior=true";
            conn = DriverManager.getConnection(url, "root", "InNtPz6E2V34");
            System.out.println("写入数据开始，成功连接MySQL SQL:" + sql);
            ps = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(Integer.MIN_VALUE);
            rs = ps.executeQuery();

            ResultSetMetaData data= rs.getMetaData();
            int colCount = data.getColumnCount();       //获取查询列数
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            while(rs.next()){
                for (int i = 1; i <= colCount; i++) {
                    columnName = data.getColumnName(i); //获取列名
                    value = rs.getString(i);
                    if (value != null && !"".equals(value.trim()) && value.trim().length() > 0) {
                        map.put(columnName, HTMLFilter.Html2Text(value));
                    } else {
                        map.put(columnName, null);
                    }
                }
                count++;
                //拼接行业字符串
                List<String> industryIds = new ArrayList<String>();
                if(map.get("industryIds")!=null){
                    String[] ls= map.get("industryIds").toString().split(",");
                    for(String id:ls){
                        industryIds.add(id);
                    }
                }
                map.put("industryIds",industryIds);

                if(map.size()>0){
                    queues.add(JSON.toJSONString(map));
                }

                if(count % 1000 == 0){
                    int number = queues.size();
                    int j = number/1000;
                    try{
                        Thread.sleep(j*1000);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int number2 = queues.size();
                    j = number2/1000;
                }
            }
            isInsert = new AtomicBoolean(false);
            return count;
        }catch (ClassNotFoundException e) {
            LOG.error("连接MYSQL 异常", e);
        }catch (SQLException e){
            LOG.error("SQL 异常", e);
        }catch(Exception e){
            LOG.error("异常:",e);
        }

        return 0;
    }

    private  boolean deleteIndexData() {
        boolean flag = false;
        int timeMillis = 60000;
        long startTime = System.currentTimeMillis();
        SearchResponse scrollResp = client.prepareSearch(INDEX)
                .setScroll(new TimeValue(timeMillis))
                .setSize(5000).execute().actionGet();
        while (true) {
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            SearchHit[] hits = scrollResp.getHits().getHits();
            System.out.println("共拉取："+hits.length+"条");
            if(hits.length > 0){
                for (SearchHit searchHit : hits) {
                    bulkRequest.add(new DeleteRequest(INDEX,TYPE,searchHit.getId()));
                }
                bulkRequest.execute().actionGet();
            }
            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId())
                    .setScroll(new TimeValue(timeMillis)).execute().actionGet();
            if (scrollResp.getHits().getHits().length == 0) {
                break;
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("删除"+INDEX+"数据共用时：" + (endTime - startTime)+"ms");
        return true;
    }
}
