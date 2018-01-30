package com.gi.xm.es.service;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.pojo.Query;
import com.gi.xm.es.pojo.query.ReportQuery;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zcy on 17-11-4.
 */
@Service
public class ReportService extends BaseService {

    @Autowired
    private Client client;


    @Value("${ctdn.report.index}")
    private  String index;

    @Value("${ctdn.report.type}")
    private  String type;

    /**构建请求体:
     *全文搜索查符合条件项目数
     * @return
     */
    public  Long queryNum(Query query){

        ReportQuery reportQuery = new ReportQuery();
        reportQuery.setKeyword(query.getKeyword());
        SearchRequestBuilder qb = queryList(reportQuery);
        SearchHits ssh = getSearchHits(qb);
        return ssh.getTotalHits();
    }

    /**构建请求体:
     * 按多条件查询项目列表
     * @param query
     * @return
     */
    public SearchRequestBuilder queryList(ReportQuery query){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        SearchRequestBuilder srb = client.prepareSearch(index);


        //按报告标题
        if (!StringUtils.isEmpty(query.getKeyword())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("title",query.getKeyword()).slop(0));
            //设置高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title");
            srb.highlighter(highlightBuilder);
        }

        // 构建builder
        srb.setQuery(queryBuilder);
        // 设置查询方式 query_then_fetch
        SearchHits  shs = getSearchHits(srb);
        Long totalHit = shs.getTotalHits();
        //设置分页参数和请求参数
        Integer tmp = query.getPageSize();
        Integer pageSize = query.getPageSize();
        Integer pageNo = query.getPageNo();
        if (pageSize*pageNo+pageSize > max_search_result){
            tmp =  max_search_result - pageSize*pageNo;
        }
        srb.setFrom(pageNo*pageSize).setSize(tmp);
        return srb;
    }

    /**
     * 查询结果转List
     * @param query
     * @param shs
     * @return
     * @throws Exception
     */
    public  List<Object> getResponseList (ReportQuery query,SearchHits shs)  throws Exception{
        List<Object> entityList = new ArrayList<>();
        for (SearchHit it : shs) {
            Map source = it.getSource();
            ReportQuery p = JSON.parseObject(JSON.toJSONString(source), ReportQuery.class);
            //获取对应的高亮域
            Map<String, HighlightField> result = it.highlightFields();
            if(result != null){
                //从设定的高亮域中取得指定域
                try{
                    for (Map.Entry<String, HighlightField> entry : result.entrySet()) {
                        String key = entry.getKey();
                        //获得高亮字段的原值
                        Field field = p.getClass().getDeclaredField(key);
                        field.setAccessible(true);
                        String value = field.get(p).toString();
                        //获得搜索关键字
                        String rep = "<firm>" + query.getKeyword() + "</firm>";
                        //替换
                        field.set(p, value.replaceAll(query.getKeyword(), rep));
                    }
                } catch (Exception e) {
                    throw e;
                }
            }
            entityList.add(p);
        }
        return entityList;
    }


    /**
     * 返回查询命中
     * @param srb
     * @return
     */
    public SearchHits getSearchHits(SearchRequestBuilder srb){
        SearchResponse res = srb.setTypes(type).setSearchType(SearchType.DEFAULT).execute().actionGet();
        SearchHits shs = res.getHits();
        return shs;
    }
}