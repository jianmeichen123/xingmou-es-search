package com.gi.xm.es.controller;

import com.gi.xm.es.pojo.Pagination;
import com.gi.xm.es.pojo.query.InvestEventQuery;
import com.gi.xm.es.service.InvestEventService;
import com.gi.xm.es.view.MessageStatus;
import com.gi.xm.es.view.Result;
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
public class InvestEventController {

    private static final Logger LOG = LoggerFactory.getLogger(InvestEventController.class);

    @Autowired
    private Client client;

    @Autowired
    private InvestEventService investEventService;

    @Value("${ctdn.invest_event.index}")
    private  String index;

    @Value("${ctdn.invest_event.type}")
    private  String type;

    @Value("${max.search.result}")
    private Integer max_search_result;


    private static Result errorRet = new Result(MessageStatus.MISS_PARAMETER.getMessage(), MessageStatus.MISS_PARAMETER.getStatus());

    @RequestMapping(value="investEvent",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result queryInvestEvent(@RequestBody InvestEventQuery investEvent) {
        Result ret = new Result();
        Integer pageSize = investEvent.getPageSize();
        Integer pageNum = investEvent.getPageNo();
        //构建请求体
        SearchRequestBuilder srb = investEventService.queryList(investEvent,index,type);
        //返回响应
        SearchHits shs = investEventService.getSearchHits(srb,type);
        Pagination page = new Pagination();
        Long totalHit = shs.getTotalHits();
        try{
            List<Object> entityList =investEventService.getResponseList (investEvent,shs);
            page.setTotal(totalHit >max_search_result?max_search_result:totalHit);
            page.setTotalhit(totalHit);
            page.setRecords(entityList);
            ret = new Result(MessageStatus.OK.getMessage(), MessageStatus.OK.getStatus(), page);
            return ret;
        }catch(Exception e){
            LOG.error(e.getMessage());
            return errorRet;
        }
    }
}