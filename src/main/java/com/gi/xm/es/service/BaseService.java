package com.gi.xm.es.service;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zcy on 17-11-4.
 */
@Configuration
public class BaseService {

    @Autowired
    private Client client;

    //查询返回最大结果条数
    @Value("${max.search.result}")
    public Integer max_search_result;


    /**
     * 构建请求体
     * @param queryBuilder
     * @param highlightBuilder
     * @param index
     * @param pageNo
     * @param pageSize
     * @return
     */
    public SearchRequestBuilder getRequestBuilder(QueryBuilder queryBuilder , HighlightBuilder highlightBuilder, String index, Integer pageNo, Integer pageSize){
        SearchRequestBuilder srb = client.prepareSearch(index);
        srb.setQuery(queryBuilder);
        srb.highlighter(highlightBuilder);
        srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        if(pageNo != null && pageSize != null){
            srb.setFrom(pageNo * pageSize).setSize(pageSize);
        }
        return srb;
    }

    public SearchRequestBuilder getRequestBuilder(QueryBuilder queryBuilder , String index, Integer pageNo, Integer pageSize){
        SearchRequestBuilder srb = client.prepareSearch(index);
        srb.setQuery(queryBuilder);
        srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        if(pageNo != null && pageSize != null){
            srb.setFrom(pageNo * pageSize).setSize(pageSize);
        }
        return srb;
    }
}
