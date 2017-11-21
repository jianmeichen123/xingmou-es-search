package com.gi.xm.es.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gi.xm.es.pojo.Pagination;
import com.gi.xm.es.pojo.query.MergeEventQuery;
import com.gi.xm.es.service.LaunchEventService;
import com.gi.xm.es.service.MergeEventService;
import com.gi.xm.es.util.ListUtil;
import com.gi.xm.es.view.MessageStatus;
import com.gi.xm.es.view.Result;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class MergeEventController {

    private static final Logger LOG = LoggerFactory.getLogger(MergeEventController.class);

    @Autowired
    private Client client;

    @Autowired
    private MergeEventService mergeEventService;

    @Value("${ctdn.merge_event.index}")
    private  String index;

    @Value("${ctdn.merge_event.type}")
    private  String type;

    @Value("${max.search.result}")
    private Integer max_search_result;


    private static Result errorRet = new Result(MessageStatus.MISS_PARAMETER.getMessage(), MessageStatus.MISS_PARAMETER.getStatus());

    @RequestMapping(value = "mergeEvent", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result queryMergeEvent(@RequestBody MergeEventQuery mergeEventQuery) {
        Result ret = new Result();
        Integer pageSize = mergeEventQuery.getPageSize();
        Integer pageNum = mergeEventQuery.getPageNo();
        //构建请求体
        SearchRequestBuilder srb = mergeEventService.queryList(mergeEventQuery);
        //返回响应
        SearchHits shs = mergeEventService.getSearchHits(srb);
        //返回响应
        Pagination page = new Pagination();
        Long totalHit = shs.getTotalHits();
        try{
            List<Object> entityList =mergeEventService.getResponseList (mergeEventQuery,shs);
            page.setTotal(totalHit >max_search_result?max_search_result:totalHit);
            page.setRecords(entityList);
            ret = new Result(MessageStatus.OK.getMessage(), MessageStatus.OK.getStatus(), page);
            ret.setTotalhit(totalHit);
            return ret;
        }catch(Exception e){
            LOG.error(e.getMessage());
            return errorRet;
        }
    }
}