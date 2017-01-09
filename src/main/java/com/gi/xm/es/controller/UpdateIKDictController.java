package com.gi.xm.es.controller;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.pojo.Query;
import com.gi.xm.es.pojo.TulingSend;
import com.gi.xm.es.pojo.UserSearchLog;
import com.gi.xm.es.service.UserLogService;
import com.gi.xm.es.util.*;
import com.gi.xm.es.view.MessageStatus;
import com.gi.xm.es.view.Pagination;
import com.gi.xm.es.view.Result;
import org.apache.http.HttpResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping(value = "/updateDict/")
public class UpdateIKDictController {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateIKDictController.class);

    /**
     * 更新ik自定义词典 @author zhangchunyuan
     */
    @RequestMapping(value="updateProject")
    public static String  writeData(HttpServletResponse response){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder("");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://10.9.130.142/edw2?characterEncoding=UTF-8&useOldAliasMetadataBehavior=true";
            conn = DriverManager.getConnection(url, "xmuser", "qcDKywE7Ka52");
            System.out.println("查询字典开始，成功连接MySQL SQL:" + "select title from edw2.dm_es_project");
            ps = conn.prepareStatement("select title from edw2.dm_es_project", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(Integer.MIN_VALUE);
            rs = ps.executeQuery();
            ResultSetMetaData data= rs.getMetaData();
            int colCount = data.getColumnCount();       //获取查询列数
            while(rs.next()) {
                for (int i = 1; i <= colCount; i++) {
                    sb.append(rs.getString(i)+"\n");
                }
            }
            response.setHeader("Last-Modified",colCount+"");
            response.setHeader("ETag",colCount+"");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }

}