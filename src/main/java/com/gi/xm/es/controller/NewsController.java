package com.gi.xm.es.controller;

import com.alibaba.fastjson.JSONObject;
import com.gi.xm.es.view.MessageInfo4ES;
import com.gi.xm.es.view.Pagination;
import com.gi.xm.es.pojo.query.NewsQuery;
import com.gi.xm.es.service.NewsService;
import com.gi.xm.es.view.MessageStatus;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class NewsController {

    private static final Logger LOG = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private Client client;

    @Autowired
    private NewsService newsService;

    @Value("${ctdn.news.index}")
    private  String index;

    @Value("${ctdn.news.type}")
    private  String type;

    @Value("${max.search.result}")
    private Integer max_search_result;


    private static MessageInfo4ES errorRet = new MessageInfo4ES(MessageStatus.MISS_PARAMETER.getStatus(),MessageStatus.MISS_PARAMETER.getMessage());

    @ApiOperation("查询资讯列表")
    @ApiImplicitParam(paramType = "body", dataType = "NewsQuery", name = "newsQuery", value = "必填项: pageNo 0开始 pageSize", required = true)
    @RequestMapping(value="news",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessageInfo4ES queryNews(@RequestBody NewsQuery newsQuery) {
        MessageInfo4ES messageInfo ;
        if(newsQuery.getPageSize()==null || newsQuery.getPageNo()==null){
            return errorRet;
        }
        //构建请求体
        SearchRequestBuilder srb = newsService.queryList(newsQuery);
        //返回响应
        SearchHits shs = newsService.getSearchHits(srb);
        Pagination page = new Pagination();
        Long totalHit = shs.getTotalHits();
        try{
            List<Object> entityList =newsService.getResponseList (newsQuery,shs);
            page.setTotal(totalHit >max_search_result?max_search_result:totalHit);
            page.setRecords(entityList);
            messageInfo = new MessageInfo4ES(MessageStatus.OK.getStatus(), MessageStatus.OK.getMessage(), page);
            messageInfo.setTotalhit(totalHit);
            return messageInfo;
        }catch(Exception e){
            LOG.error(e.getMessage());
            return errorRet;
        }
    }
    
    /**
     * 高管-首页-获取最近三天行业资讯
     */
    @RequestMapping(value="getGGNews",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessageInfo4ES getGGNews(@RequestBody NewsQuery newsQuery) {
    	MessageInfo4ES ret ;
        try{
        	Pagination page = new Pagination();
        	List<SearchHits>  result = newsService.queryGGList(newsQuery);
        	List<List<Object>> entityList = new ArrayList<List<Object>>();
        	for(SearchHits searchHits : result){
        		List<Object> objList =newsService.getResponseList (newsQuery,searchHits);
        		entityList.add(objList);
        	}
            page.setRecordList(entityList);
            ret = new MessageInfo4ES(MessageStatus.OK.getStatus(), MessageStatus.OK.getMessage(), page);
            return ret;
        }catch(Exception e){
            LOG.error(e.getMessage());
            return errorRet;
        }
    }
    
    
    //高管-首页-竞争状态
    @RequestMapping(value="getGGCompeteInfo",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessageInfo4ES getGGCompeteInfo(@RequestBody NewsQuery newsQuery) {
    	MessageInfo4ES ret ;
        try{
        	Pagination page = new Pagination();
        	SearchHits  result = newsService.getGGCompeteInfo(newsQuery);
        	List<Object> records =newsService.getResponseList (newsQuery,result);
            page.setRecords(records);
            ret = new MessageInfo4ES(MessageStatus.OK.getStatus(), MessageStatus.OK.getMessage(), page);
            return ret;
        }catch(Exception e){
            LOG.error(e.getMessage());
            return errorRet;
        }
    }

    @ApiOperation("app端查询资讯接口,返回分类数据和条数")
    @ApiImplicitParam(paramType = "body", dataType = "NewsQuery", name = "newsQuery", value = "必填项: pageSize keyword:搜索关键字 结果取data:分组统计数据字段", required = true)
    @RequestMapping(value="getAggregationNews",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessageInfo4ES getAppNews(@RequestBody NewsQuery newsQuery) {
        if(newsQuery.getPageSize()==null || newsQuery.getKeyword() ==null){
            return errorRet;
        }
        //构建请求体
        SearchRequestBuilder srb = client.prepareSearch(index)
                                         .setTypes(type)
                                         .setQuery(QueryBuilders.termQuery("title",newsQuery.getKeyword().toLowerCase()))
                                         .addAggregation(AggregationBuilders.terms("perType").field("typeId").subAggregation(AggregationBuilders.topHits("topHit").size(newsQuery.getPageSize())));
        //返回响应
        SearchHits shs = newsService.getSearchHits(srb);
        MessageInfo4ES result = newsService.getAggregationResponse(newsQuery,srb);
        return result;
    }
}