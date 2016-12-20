/*
package com.gi.xm.es.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

*/
/**
 * Created by zcy on 16-12-12.
 *//*

public class ESDataUtil {

    static ConcurrentLinkedQueue<String> queues = new ConcurrentLinkedQueue<String>();
    static AtomicBoolean isInsert = new AtomicBoolean(true);
    static final String HOST = "10.9.130.135";
    static final String clustername = "elasticsearch";
    static TransportClient client = null;
    private static final Logger LOG = LoggerFactory.getLogger(ESDataUtil.class);

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
        //importInvestfirms();
        importInvestor();
        //importOriginator();
    }

    */
/**
     *  项目
     *//*

    public static void importProjects(){
        createIndex("xm_project_a","project");
        String projectSql = "select " +
                "p.id as sid," +
                "p.title," +
                "p.description," +
                "p.pic_big_xm as logo, " +
                "i.icon as icon ," +
                "p.industry_name as labels," +
                "p.industry_name as indudstryName ," +
                "p.industry_sub_name as indudstrySubName," +
                "p.newest_event_round as roundName," +
                "p.create_date as createDate " +
                "from edw2.dm_es_project p left join edw2.dw_v_industry  i on  p.industry_id = i.id ";
        excuteThread("xm_project_a","project",projectSql);
    }

    */
/**
     *  投资机构
     *//*

    public static void importInvestfirms(){
        String investfirmsSql = " select " +
                " i.name, " +
                " i.description, " +
                " d.invest_industry as investIndustry," +
                " d.invest_round as roundNames," +
                " d.newest_invest_projects as recentProjects," +
                " d.newest_invest_projects_ids as projectIds," +
                " i.id as sid," +
                " i.icon_big_xm as logo" +
                " from edw2.dm_investfirms i left join edw2.dm_investfirms_data d  on d.investfirm_id = i.id";
        excuteThread("xm_investfirm_a","investfirm",investfirmsSql);
    }

    */
/**
     * 创始人
     *//*

    public static void importOriginator(){
        String originatorSql = "select " +
                "ps.name," +
                "pj.title as projectName," +
                "ps.postion_name as position," +
                "ps.schools as schoolNames," +
                "ps.description as jobDescription," +
                "ps.icon_xm as avatar " +
                "from edw2.dm_project_person ps," +
                "edw2.dm_project pj " +
                "where ps.project_id = pj.id" ;
        excuteThread("xm_originator_a","originator",originatorSql);
    }

    */
/**
     *  投资人
     *//*

    public static void importInvestor(){

        String investorSql = "select " +
                "name," +
                "investfirm_name as investfirmName," +
                "investfirm_postion_name  as position," +
                "description," +
                "id as sid," +
                "icon_xm as avatar " +
                "from edw2.dm_investor";
        excuteThread("xm_investor_a","investor",investorSql);

    }

    public static void excuteThread(String index,String type,String sql){
        long startTime = System.currentTimeMillis();
        createIndex(index,type);
        int rowcount = writeData(sql);
        long endTime = System.currentTimeMillis();
        System.out.println(index+"数据写入完毕");
        System.out.println("总用时:"+(endTime - startTime)+"ms");
        System.out.println("总条数:"+rowcount+"条");
    }

    public static void  createIndex(final  String index,final  String type){
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
                                    if (bulkResponse.hasFailures()) {
                                        System.out.println("请求数量："+ bulkRequest.numberOfActions());
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
                            bulkProcessor.close();
                            break;

                        }
                    }
                }
            }).start();
        }
    }



    */
/**
     * 读取mysql 数据
     * @param sql 查询语句
     *//*

    public static int writeData(String sql){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        String columnName = null;
        String value = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://10.9.130.142/edw2?characterEncoding=UTF-8&useOldAliasMetadataBehavior=true";
            conn = DriverManager.getConnection(url, "xmuser", "qcDKywE7Ka52");
            System.out.println("写入数据开始，成功连接MySQL SQL:" + sql);
            ps = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(Integer.MIN_VALUE);
            rs = ps.executeQuery();

            ResultSetMetaData data= rs.getMetaData();
            int colCount = data.getColumnCount();       //获取查询列数
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            while(rs.next()){
                for(int i = 1; i<= colCount; i++){
                    columnName = data.getColumnName(i); //获取列名
                        value = rs.getString(i);
                        if (value != null && !"".equals(value.trim()) && value.trim().length() > 0) {
                            map.put(columnName,value);
                        }else{
                            map.put(columnName,"");
                        }
                }
                count++;

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
                e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

}
*/
