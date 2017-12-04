package com.gi.xm.es.controller;

import com.gi.xm.es.view.MessageInfo4ES;
import com.gi.xm.es.view.Pagination;
import com.gi.xm.es.pojo.query.ProjectQuery;
import com.gi.xm.es.service.ProjectService;
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
public class ProjectController {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private Client client;

    @Autowired
    private ProjectService projectService;

    @Value("${max.search.result}")
    private Integer max_search_result;

    private static MessageInfo4ES errorRet = new MessageInfo4ES(MessageStatus.MISS_PARAMETER.getStatus(),MessageStatus.MISS_PARAMETER.getMessage());

    @RequestMapping(value = "project", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessageInfo4ES queryProject(@RequestBody ProjectQuery project) {
        MessageInfo4ES messageInfo = new MessageInfo4ES();
        Integer pageSize = project.getPageSize();
        Integer pageNum = project.getPageNo();
        //构建请求体
        SearchRequestBuilder srb = projectService.queryList(project);
        //返回响应
        SearchHits shs = projectService.getSearchHits(srb);
        Pagination page = new Pagination();
        Long totalHit = shs.getTotalHits();
        try{
            List<Object> entityList =projectService.getResponseList (project,shs);
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