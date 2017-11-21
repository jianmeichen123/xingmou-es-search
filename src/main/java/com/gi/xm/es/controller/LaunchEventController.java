package com.gi.xm.es.controller;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.pojo.Pagination;
import com.gi.xm.es.pojo.query.InvestEventQuery;
import com.gi.xm.es.pojo.query.LaunchEventQuery;
import com.gi.xm.es.service.LaunchEventService;
import com.gi.xm.es.util.ListUtil;
import com.gi.xm.es.view.MessageStatus;
import com.gi.xm.es.view.Result;
import org.apache.lucene.queryparser.classic.QueryParserBase;
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
public class LaunchEventController {

    private static final Logger LOG = LoggerFactory.getLogger(LaunchEventController.class);

    @Autowired
    private Client client;

    @Autowired
    private LaunchEventService launchEventService;

    @Value("${ctdn.launch_event.index}")
    private  String index;

    @Value("${ctdn.launch_event.type}")
    private  String type;

    @Value("${max.search.result}")
    private Integer max_search_result;


    private static Result errorRet = new Result(MessageStatus.MISS_PARAMETER.getMessage(), MessageStatus.MISS_PARAMETER.getStatus());

    @RequestMapping(value="launchEvent",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result queryInvestEvent(@RequestBody LaunchEventQuery launchEventQuery) {
        Result ret = new Result();
        Integer pageSize = launchEventQuery.getPageSize();
        Integer pageNum = launchEventQuery.getPageNo();
        //构建请求体
        SearchRequestBuilder srb = launchEventService.queryList(launchEventQuery);
        //返回响应
        SearchHits shs = launchEventService.getSearchHits(srb);
        Long totalHit = shs.getTotalHits();
        Pagination page = new Pagination();
        try{
            List<Object> entityList =launchEventService.getResponseList (launchEventQuery,shs);
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