package com.gi.xm.es.controller;

import com.gi.xm.es.view.MessageInfo4ES;
import com.gi.xm.es.view.Pagination;
import com.gi.xm.es.pojo.query.NewsQuery;
import com.gi.xm.es.service.NewsService;
import com.gi.xm.es.view.MessageStatus;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value="news",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessageInfo4ES queryNews(@RequestBody NewsQuery newsQuery) {
        MessageInfo4ES messageInfo ;
        Integer pageSize = newsQuery.getPageSize();
        Integer pageNum = newsQuery.getPageNo();
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
}