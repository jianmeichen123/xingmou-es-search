package com.gi.xm.es.util;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.dbutil.ConnectionManager;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

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
public class Mysql2ES {

    static ConcurrentLinkedQueue<String> queues = new ConcurrentLinkedQueue<String>();
    private static final Logger LOG = LoggerFactory.getLogger(Mysql2ES.class);
    static AtomicBoolean isInsert = new AtomicBoolean(true);
    static final String HOST = "10.9.130.135";
    static final String PORT = "9200";
    static final String clustername = "elasticsearch";
    static TransportClient client = null;
    static String[] proHeader = new String[]{"projectId","code","industryIds","industryName","industrySubName","districtId","districtSubId","districtSubName","logoSmall","projTitle","setupDT","latestFinanceRound","latestFinanceDT","latestFinanceAmountStr","latestFinanceAmountNum","currencyTitle","loadDate"};
    static String[] investEventHeader = new String[]{"eventId","code","sourceId","sourceCode","industryIds","industryName","industrySubName","round","districtId","districtSubId","districtSubName","logo","company","investDate","amountStr","amountNum","currencyTitle","investSideJson","bodyRole","sourceType","isClick"};
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
        //importProjects();
        importInvestEvent();
    }
    /**
     *  项目
     */
    public static void importProjects(){
        deleteIndexData("ctdn_project","project");
        String sql = "select projectId,code,industryIds,industryName,industrySubName,districtId,districtSubId,districtSubName,"+
                "logoSmall,projTitle,setupDT,latestFinanceRound,latestFinanceDT,latestFinanceAmountStr,latestFinanceAmountNum,currencyTitle,loadDate "+
                "from app.app_project_info where projectId > ? and projectId <= ?";
        excuteThread("ctdn_project","project",sql,"app_project_info",proHeader);
    }
    /**
     * 投资事件
     */
    public static void importInvestEvent(){
        deleteIndexData("ctdn_invest_event","invest_event");
        String sql = "select eventId,code,sourceId,sourceCode,industryIds,industryName,industrySubName,round,districtId,districtSubId,"+
                    "districtSubName,logo,company,investDate,amountStr,amountNum,currencyTitle,investSideJson,bodyRole,sourceType,isClick "+
                    "from app.app_event_info where eventId > ? and eventId <= ?";
        excuteThread("ctdn_invest_event", "invest_event", sql,"app_event_info",investEventHeader);
    }

    /**
     *   删除重建索引
     */
    private static void deleteIndexData(String index,String type) {
        try {
            Runtime.getRuntime().exec("curl -XDELETE "+HOST+":9200/"+index);
            String command = "curl -XPUT "+HOST+":9200/"+index +" -d "+ESEXEC.ADDINDEX.get(index).toJSONString();
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void excuteThread(String index,String type,String sql,String tableName,String[] headerTitle){
        createIndex(index,type);
        Long rowcount = writeData(sql,tableName,headerTitle);
        System.out.println("总条数:"+rowcount+"条");
    }

    public static long  createIndex( final String index,  final String type){
        final ConcurrentHashMap<String, Boolean> hashMap = new ConcurrentHashMap();
        ExecutorService exe = Executors.newFixedThreadPool(5);
        //开多线程读队列的数据
        for(int t =0 ;t<3; t++){
            exe.execute(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            hashMap.put(Thread.currentThread().getName(), Boolean.FALSE);
                            BulkProcessor bulkProcessor = BulkProcessorSingleTon.INSTANCE.getInstance(clustername,HOST);
                            //读取队列里的数据
                            while(true){
                                if(!queues.isEmpty()){
                                     String json = queues.poll();
                                    if(json == null) continue;
                                    bulkProcessor.add(new IndexRequest(index,type).source(json));
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
                                        bulkProcessor.awaitClose(5, TimeUnit.SECONDS);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    break;
                                }
                            }

                        }
                    }
                    )
            );
        }
        exe.shutdown();
        return System.currentTimeMillis();
    }


    /**
     * 读取mysql 数据
     * @param sql 查询语句
     */
    public static Long writeData(String sql,String tabelName,String[] headerTitle) {
        Long start = System.currentTimeMillis();
        String value = null;
        int limit = 10000;
        int from = 0;
        int to = limit;
        Long tmp = 0l;
        Long total = 0l;
        try {
            //查询总数
            Connection connection = ConnectionManager.getInstance().getConnection();
            Statement stmt = connection.createStatement();
            ResultSet ret = stmt.executeQuery("select count(*) from " + tabelName);
            while (ret.next()) {
                total = ret.getLong(1);
            }
            ret.close();
            stmt.close();
            connection.close();
            //每次读取1w条数据
            while (true) {
                ConnectionManager cm = ConnectionManager.getInstance();
                Connection conn = cm.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                ps.setInt(1, from);
                ps.setInt(2, to);
                ps.setFetchSize(limit);
                ResultSet rs = ps.executeQuery();

                String columnName = null;
                LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
                int perCount = 0;
                if (tmp >= total) {
                    break;
                }
                while (rs.next()) {
                    for (int i = 1; i <= headerTitle.length; i++) {
                        columnName = headerTitle[i - 1]; //获取列名
                        value = rs.getString(i);
                        if (!StringUtils.isEmpty(value)) {
                            map.put(columnName, value);
                        } else {
                            map.put(columnName, null);
                        }
                    }
                    perCount++;
                    if (map.get("industryIds") != null) {
                        List<String> industryIds = new ArrayList<String>();
                        String[] ls = map.get("industryIds").toString().split(",");
                        for (String id : ls) {
                            industryIds.add(id);
                        }
                        map.put("industryIds", industryIds);
                    }
                    if (map.size() > 0) {
                        queues.add(JSON.toJSONString(map));
                    }
//                    if(perCount % 5000 == 0){
//                       int number = queues.size();
//                        int j = number/5000;
//                        Thread.sleep(j*1000);
//                    }
                }
                tmp += perCount;
                from += limit;
                to += limit;
                ps.close();
                rs.close();
                conn.close();
            }
            isInsert = new AtomicBoolean(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("mysql 用时:" + (System.currentTimeMillis() - start) + " ms");
        return total;
    }

}
