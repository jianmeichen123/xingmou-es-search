package com.gi.xm.es.controller;

import com.gi.xm.es.pojo.query.ReportQuery;
import com.gi.xm.es.service.ReportService;
import com.gi.xm.es.view.MessageInfo4ES;
import com.gi.xm.es.view.MessageStatus;
import com.gi.xm.es.view.Pagination;
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
public class ReportController {

    private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private Client client;

    @Autowired
    private ReportService service;

    @Value("${max.search.result}")
    private Integer max_search_result;

    private static MessageInfo4ES errorRet = new MessageInfo4ES(MessageStatus.MISS_PARAMETER.getStatus(),MessageStatus.MISS_PARAMETER.getMessage());

    @RequestMapping(value = "report", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessageInfo4ES queryReport(@RequestBody ReportQuery query) {
        MessageInfo4ES messageInfo = new MessageInfo4ES();
       /* Integer pageSize = query.getPageSize();
        Integer pageNum = query.getPageNo();*/
        //构建请求体
        SearchRequestBuilder srb = service.queryList(query);
        //返回响应
        SearchHits shs = service.getSearchHits(srb);
        Pagination page = new Pagination();
        Long totalHit = shs.getTotalHits();
        try{
            List<Object> entityList =service.getResponseList (query,shs);
            page.setTotal(totalHit >max_search_result?max_search_result:totalHit);
            page.setRecords(entityList);
            messageInfo = new MessageInfo4ES(MessageStatus.OK.getStatus(),MessageStatus.OK.getMessage(), page);
            messageInfo.setTotalhit(totalHit);
            return messageInfo;
        }catch(Exception e){
            LOG.error(e.getMessage());
            return errorRet;
        }
    }
}