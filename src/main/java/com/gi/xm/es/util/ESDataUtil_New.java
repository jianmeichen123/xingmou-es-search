package com.gi.xm.es.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*/

/**
 * Created by zcy on 16-12-12.
 */
public class ESDataUtil_New {

    static ConcurrentLinkedQueue<String> queues = new ConcurrentLinkedQueue<String>();
    static AtomicBoolean isInsert = new AtomicBoolean(true);
    static final String HOST = "127.0.0.1";
    static final String clustername = "elasticsearch";
    static TransportClient client = null;
  //  private static final Logger LOG = LoggerFactory.getLogger(ESDataUtil.class);

    //连接es client
    static {
        try {
            Settings settings = Settings.builder()
                    .put("cluster.name", clustername).build();
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOST), 9300));
        } catch (UnknownHostException e) {
            //LOG.error("连接 client 异常", e);
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {

       importProjects();
       importInvestfirms();
       importInvestEvent();
       importMergeEvent();
       importQuitEvent();
    }

    /**
     *  项目
     */
    public static void importProjects(){
        boolean isDelete = deleteIndexData("ctdn_project","project");
        if(isDelete){
            String sql = "select " +
                    "code," +
                    "id as sourceId,"+
                    "industryName,"+
                    "industrySubName,"+
                    "industryGrandSonName,"+
                    "industryIds,"+
                    "districtId,"+
                    "districtSubId,"+
                    "addr,"+
                    "logoSmall,"+
                    "projTitle,"+
                    "setupDT,"+
                    "latestFinanceRound,"+
                    "latestFinanceDT,"+
                    "latestFinanceAmountStr,"+
                    "latestFinanceAmountNum,"+
                    "loadDate "+
                    "from app.app_project_info";
            excuteThread("ctdn_project","project",sql);
        }
    }

    /**
     *  投资机构
     */
    public static void importInvestfirms(){
        boolean isDelete = deleteIndexData("ctdn_investfirms","investfirms");
        if(isDelete) {
            String sql = "select " +
                    "code," +
                    "id as sourceId,"+
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
            excuteThread("ctdn_investfirms", "investfirms", sql);
        }
    }

    /**
     * 投资事件
     */
    public static void importInvestEvent(){
        boolean isDelete = deleteIndexData("ctdn_invest_event","investEvent");
        if(isDelete) {
            String sql = "select " +
                    "code," +
                    "id as sourceId,"+
                    "industryIds,"+
                    "round,"+
                    "districtId,"+
                    "districtSubId,"+
                    "district,"+
                    "logo,"+
                    "company,"+
                    "investdate,"+
                    "amountStr,"+
                    "amountNum,"+
                    "currencyTitle,"+
                    "investSideJson,"+
                    "bodyRole,"+
                    "sourceType,"+
                    "isClick "+
                    "from app.app_event_info";
            excuteThread("ctdn_invest_event", "invest_event", sql);
        }
    }

    /**
     *  并购事件
     */
    public static void importMergeEvent(){
        boolean isDelete = deleteIndexData("ctdn_merge_event","mergeEvent");
        if(isDelete) {
            String sql = "select " +
                    "code," +
                    "id as sourceId,"+
                    "industryIds,"+
                    "district,"+
                    "mergeType,"+
                    "mergeState,"+
                    "currencyTitle,"+
                    "equityRate,"+
                    "mergeBeginDate,"+
                    "mergeEndDate,"+
                    "mergeOrderDate,"+
                    "logo,"+
                    "projTitle,"+
                    "amountStr,"+
                    "amountNum,"+
                    "mergeSideJson,"+
                    "bodyRole,"+
                    "sourceType,"+
                    "isClick "+
                    "from app.app_project_merger";
            excuteThread("ctdn_merge_event", "merge_event", sql);
        }
    }

    /**
     *  退出事件
     */
    public static void importQuitEvent(){
        boolean isDelete = deleteIndexData("ctdn_quit_event","quit_event");
        if(isDelete) {
            String sql = "select " +
                    "code," +
                    "id as sourceId,"+
                    "industryIds,"+
                    "district,"+
                    "quitType,"+
                    "districtId,"+
                    "districtSubId,"+
                    "district,"+
                    "logo,"+
                    "company,"+
                    "quitDate,"+
                    "quitAmountStr,"+
                    "quitAmountNum,"+
                    "currencyTitle,"+
                    "quitSideJson,"+
                    "bodyRole,"+
                    "sourceType,"+
                    "isClick "+
                    "from app.app_event_quit_info";
            excuteThread("ctdn_quit_event", "quit_event", sql);
        }
    }


    public static void excuteThread(String index,String type,String sql){
        long startTime = System.currentTimeMillis();
        createIndex(index,type);
        int rowcount = writeData(sql,type);
        long endTime = System.currentTimeMillis();
        System.out.println(index+"数据写入完毕");
        System.out.println("总用时:"+(endTime - startTime)+"ms");
        System.out.println("总条数:"+rowcount+"条");
    }

    public static void  createIndex( final String index,  final String type){
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
                            bulkProcessor.add(new IndexRequest(index,type).source(json));
                            currentCount++;
                        }
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
                                bulkProcessor.awaitClose(10, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
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
    public static int writeData(String sql,String type){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count =0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://10.9.130.142/edw2?characterEncoding=UTF-8&useOldAliasMetadataBehavior=true";
            conn = DriverManager.getConnection(url, "root", "IhNtPz6E2V34");
            System.out.println("写入数据开始，成功连接MySQL SQL:" + sql);
            ps = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(Integer.MIN_VALUE);
            rs = ps.executeQuery();
            switch (type){
                case "project" :{
                    count = readProjectRet(rs);
                }
            }
            isInsert = new AtomicBoolean(false);
            return count;
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    private static boolean deleteIndexData(String index,String type) {
        boolean flag = false;
        int timeMillis = 60000;
        long startTime = System.currentTimeMillis();
        SearchResponse scrollResp = client.prepareSearch(index)
                .setScroll(new TimeValue(timeMillis))
                .setSize(5000).execute().actionGet();
        while (true) {
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            SearchHit[] hits = scrollResp.getHits().getHits();
            System.out.println("共拉取："+hits.length+"条");
            if(hits.length > 0){
                for (SearchHit searchHit : hits) {
                    bulkRequest.add(new DeleteRequest(index,type,searchHit.getId()));
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
        System.out.println("删除"+index+"数据共用时：" + (endTime - startTime)+"ms");
        return true;
    }

    private static  int  readProjectRet(ResultSet rs) {
        String columnName = null;
        String value = null;
        int count = 0;
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        try {
            ResultSetMetaData data = rs.getMetaData();
            int colCount = data.getColumnCount();       //获取查询列数
            while (rs.next()) {
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
                List<String> industryIds = new ArrayList<String>();
                if(map.get("industryIds")!=null){
                    String[] ls= map.get("industryIds").toString().split(",");
                    for(String id:ls){
                        industryIds.add(id);
                    }
                }
                map.put("industryIds",industryIds);

                if (map.size() > 0) {
                    queues.add(JSON.toJSONString(map));
                }

                if (count % 1000 == 0) {
                    int number = queues.size();
                    int j = number / 1000;
                    try {
                        Thread.sleep(j * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int number2 = queues.size();
                    j = number2 / 1000;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}

