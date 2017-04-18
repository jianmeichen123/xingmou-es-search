package com.gi.xm.es.controller;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.pojo.ProjectNew;
import com.gi.xm.es.pojo.Query;
import com.gi.xm.es.pojo.TulingSend;
import com.gi.xm.es.pojo.UserSearchLog;
import com.gi.xm.es.service.UserLogService;
import com.gi.xm.es.util.Aes;
import com.gi.xm.es.util.EntityUtil;
import com.gi.xm.es.util.Md5;
import com.gi.xm.es.util.PostServer;
import com.gi.xm.es.view.MessageStatus;
import com.gi.xm.es.view.Pagination;
import com.gi.xm.es.view.Result;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.apache.lucene.search.BooleanQuery;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.*;

@RestController
@RequestMapping(value = "/search/")
public class MultiSearchControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(MultiSearchControllerTest.class);

    @Autowired
    private Client client;

    @Autowired
    private UserLogService userLogService;

    private static final String INDEX = "ctdn_project";

    private static final String TYPE = "project";

    private static final Integer SEARCHLIMIT = 5000;

    private static List<Object> emtpyList  = new ArrayList<Object>();

    private static Result errorRet = new Result(MessageStatus.MISS_PARAMETER.getMessage(), MessageStatus.MISS_PARAMETER.getStatus());




    @RequestMapping(value="searchProject",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result searchByKey(@RequestBody ProjectNew project) {
        Result ret = new Result();
        Integer pageSize = project.getPageSize();
        Integer pageNum = project.getPageNo();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //按行业
        if(project.getIndustryList() != null && !project.getIndustryList().isEmpty() ){
            queryBuilder.must(QueryBuilders.termsQuery("industrySearch",project.getIndustryList()));
        }

        //按title
        if(project.getTitle() != null){
            queryBuilder.must(QueryBuilders.wildcardQuery("title","*"+project.getTitle()+"*"));
        }

        //按createDate
        if(project.getStartDate() != null || project.getEndDate() != null){
            RangeQueryBuilder rangeq = QueryBuilders.rangeQuery("createDate");
            if(project.getStartDate() != null ){
                rangeq.gte(project.getStartDate());
            }
            if(project.getEndDate() != null ){
                rangeq.lte(project.getEndDate());
            }
            queryBuilder.filter(rangeq);
        }

        //按round
        if(project.getRoundList()  != null && !project.getRoundList().isEmpty() ){
            queryBuilder.must(QueryBuilders.termsQuery("newestEventRound",project.getRoundList()));
        }

        //按地区
        if(project.getDistrictIds() != null && !project.getDistrictIds().isEmpty()){
            queryBuilder.must(QueryBuilders.termsQuery("districtId",project.getDistrictIds()));
        }
        if(project.getDistrictSubIds() != null && !project.getDistrictSubIds().isEmpty()){
            queryBuilder.must(QueryBuilders.termsQuery("districtSubId",project.getDistrictSubIds()));
        }
        //设置分页参数和请求参数
        SearchRequestBuilder sb = client.prepareSearch(INDEX);
        sb.setQuery(queryBuilder);
        if(pageNum * pageSize > SEARCHLIMIT){
            sb.setFrom(SEARCHLIMIT/pageSize);
        }else{
            sb.setFrom(pageNum);
        }
        sb.setSize(pageSize);

        if(project.getOrder() != null){
            switch (project.getOrder()){
                case "createDate":{
                    sb.addSort("createDate", SortOrder.fromString(project.getOrderBy()));
                }
                case "newestEventDate":{
                    sb.addSort("newestEventDate", SortOrder.fromString(project.getOrderBy()));
                }
            }
        }else{
            sb.addSort("createDate",SortOrder.DESC);
        }

        //返回响应
        SearchResponse res =sb.setTypes(TYPE).setSearchType(SearchType.DEFAULT).execute().actionGet();
        SearchHits shs = res.getHits();

        List<Object> projectNewList = new ArrayList<>();
        long count=0;
        for (SearchHit it : shs) {
            count++;
            Map source = it.getSource();
            ProjectNew projectNew =  JSON.parseObject(JSON.toJSONString(source),ProjectNew.class);
            projectNewList.add(projectNew);
        }
        Pagination page = new Pagination();
        page.setTotal(count);
        page.setRecords(projectNewList);
        ret = new Result(MessageStatus.OK.getMessage(), MessageStatus.OK.getStatus(), page);
        return ret;
    }



   /* private SearchRequestBuilder queryProject(@RequestBody ProjectNew project){
        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.field("description");
        highlightBuilder.preTags("<span class = 'highlight'>");
        highlightBuilder.postTags("</span>");
        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("title",keyword))
                .add(QueryBuilders.matchQuery("description", keyword).analyzer("ik_smart"))
                .add(QueryBuilders.termQuery("lables", keyword));
                *//*.add(QueryBuilders.termQuery("indudstryName", keyword))
                .add(QueryBuilders.termQuery("indudstrySubName", keyword))
                .add(QueryBuilders.termQuery("roundName", keyword));*//*
        SearchRequestBuilder srb = getRequestBuilder(qb,highlightBuilder,EntityUtil.PROJECT_INDEX_A,pageNo,pageSize);
        return srb;
    }

    private SearchRequestBuilder queryInvestFirm(String keyword, Integer pageNo,Integer pageSize){
        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.field("description");
        highlightBuilder.preTags("<span class = 'highlight'>");
        highlightBuilder.postTags("</span>");

        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("name", keyword))
                .add(QueryBuilders.matchQuery("description", keyword).analyzer("ik_smart"))
                .add(QueryBuilders.termQuery("areaNames", keyword))
                .add(QueryBuilders.termQuery("roundNames", keyword));
        SearchRequestBuilder srb = getRequestBuilder(qb,highlightBuilder,EntityUtil.INVESTFIRM_INDEX_A,pageNo,pageSize);
        return srb;
    }

    private SearchRequestBuilder queryInvestor(String keyword,  Integer pageNo,Integer pageSize){
        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.field("investfirmName");
        highlightBuilder.field("description");
        highlightBuilder.preTags("<span class = 'highlight'>");
        highlightBuilder.postTags("</span>");

        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("name", keyword))
                .add(QueryBuilders.matchQuery("description", keyword).analyzer("ik_smart"))
                .add(QueryBuilders.termQuery("investfirmName", keyword))
                .add(QueryBuilders.rangeQuery("date").gt("ee").lt("ww"));
        SearchRequestBuilder srb = getRequestBuilder(qb,highlightBuilder,EntityUtil.INVESTOR_INDEX_A,pageNo,pageSize);
        return srb;
    }
    private SearchRequestBuilder queryOriginator(String keyword, Integer pageNo,Integer pageSize){
        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.field("jobDescription");
        highlightBuilder.field("projectName");
        highlightBuilder.preTags("<span class = 'highlight'>");
        highlightBuilder.postTags("</span>");
        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("name", keyword))
                .add(QueryBuilders.termQuery("projectName", keyword))
                .add(QueryBuilders.matchQuery("jobDescription", keyword).analyzer("ik_smart"));
        SearchRequestBuilder srb = getRequestBuilder(qb,highlightBuilder,EntityUtil.ORIGINATOR_INDEX_A,pageNo,pageSize);
        return srb;
    }

    private SearchRequestBuilder getRequestBuilder(QueryBuilder queryBuilder ,HighlightBuilder highlightBuilder, String index, Integer pageNo,Integer pageSize){

        SearchRequestBuilder srb = client.prepareSearch(index);
        srb.setQuery(queryBuilder);
        srb.highlighter(highlightBuilder);
        srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        if(pageNo != null && pageSize != null){
            srb.setFrom(pageNo * pageSize).setSize(pageSize);
        }
        return srb;
    }

*/
}