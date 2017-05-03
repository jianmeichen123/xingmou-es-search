package com.gi.xm.es.util;

import java.sql.*;

/**
 * Created by zcy on 17-5-3.
 */
public class TEST2 {

    public static void main(String[] args){
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
        String sql = "select title from dm_project where id > ? and id <= ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://10.9.130.142/edw2?characterEncoding=UTF-8&useOldAliasMetadataBehavior=true";
            conn = DriverManager.getConnection(url, "xmuser", "qcDKywE7Ka52");
            System.out.println("写入数据开始，成功连接MySQL SQL:" + sql);

           for(int i = 0;i<=2000;i++){
               System.out.println("i1:"+i);
               ps = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
               System.out.println("i2:"+i);
               ps.setInt(1, from);
               ps.setInt(2, to);
               ps.setFetchSize(limit);
               rs = ps.executeQuery();
               ResultSetMetaData data = rs.getMetaData();
               int colCount = data.getColumnCount();
               int perCount = 0;
               while(rs.next()){
                   perCount = perCount+1;
                   for(int j = 1; j<colCount;j++){
                       rs.getString(j);
                   }
               }
               total+=perCount;
               from+=limit;
               to +=limit;
               System.out.println(perCount+":"+total);
               System.out.println("from :"+from+" to:"+to);
           }
        }catch (Exception e){
               e.printStackTrace();}
        }
}
