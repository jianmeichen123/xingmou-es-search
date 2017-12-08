package com.gi.xm.es.controller;

import com.gi.xm.es.view.MessageInfo4ES;
import com.gi.xm.es.view.Pagination;
import com.gi.xm.es.pojo.query.LaunchEventQuery;
import com.gi.xm.es.service.LaunchEventService;
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


    private static MessageInfo4ES errorRet = new MessageInfo4ES(MessageStatus.MISS_PARAMETER.getStatus(),MessageStatus.MISS_PARAMETER.getMessage());

    @RequestMapping(value="launchEvent",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessageInfo4ES queryInvestEvent(@RequestBody LaunchEventQuery launchEventQuery) {
        MessageInfo4ES messageInfo ;
        if(launchEventQuery.getPageSize()==null || launchEventQuery.getPageNo()==null){
            return errorRet;
        }
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
            messageInfo = new MessageInfo4ES( MessageStatus.OK.getStatus(),MessageStatus.OK.getMessage(), page);
            messageInfo.setTotalhit(totalHit);
            return messageInfo;
        }catch(Exception e){
            LOG.error(e.getMessage());
            return errorRet;
        }
    }
}