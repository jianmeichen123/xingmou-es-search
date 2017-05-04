package com.gi.xm.es.util;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zcy on 17-5-3.
 */
public class TEST2 {
    static ConcurrentLinkedQueue<String> queues = new ConcurrentLinkedQueue<String>();
    static AtomicBoolean isInsert = new AtomicBoolean(true);
    static final String HOST = "10.9.130.135";
    static final String clustername = "elasticsearch";
    static TransportClient client = null;
    private static final Logger LOG = LoggerFactory.getLogger(TEST.class);

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

    public static void main(String[] args) {
        writeData("select title from dm_project where id > ? and id<= ?");
    }
}
