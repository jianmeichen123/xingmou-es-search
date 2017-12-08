package com.gi.xm.es.controller;

import com.gi.xm.es.view.MessageInfo4ES;
import com.gi.xm.es.view.Pagination;
import com.gi.xm.es.pojo.query.ProjectQuery;
import com.gi.xm.es.service.ProjectService;
import com.gi.xm.es.view.MessageStatus;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("查询项目列表")
    @ApiImplicitParams(
            {@ApiImplicitParam(paramType = "query", dataType = "String", name = "keyword", value = "项目查询", required = false),
             @ApiImplicitParam(paramType = "query", dataType = "List<Integer>", name = "districtIds", value = "项目一级地区id集合", required = false),
             @ApiImplicitParam(paramType = "query", dataType = "List<Integer>", name = "districtSubIds", value = "项目查询", required = false),
             @ApiImplicitParam(paramType = "query", dataType = "String", name = "endDate", value = "截止日期", required = false),
             @ApiImplicitParam(paramType = "query", dataType = "String", name = "startDate", value = "开始日期", required = false),
             @ApiImplicitParam(paramType = "query", dataType = "List<Integer>", name = "industryIds", value = "一级行业id集合", required = false),
             @ApiImplicitParam(paramType = "query", dataType = "String", name = "order", value = "asc/desc", required = false),
             @ApiImplicitParam(paramType = "query", dataType = "String", name = "orderBy", value = "排序字段", required = false),
             @ApiImplicitParam(paramType = "query", dataType = "integer", name = "pageNo", value = "当前页码 从0 开始", required = true),
             @ApiImplicitParam(paramType = "query", dataType = "integer", name = "pageSize", value = "", required = true),
             @ApiImplicitParam(paramType = "query", dataType = "List<s>", name = "rounds", value = "轮次名称集合", required = false)
            })
    @RequestMapping(value = "project", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessageInfo4ES queryProject(@RequestBody ProjectQuery project) {
        MessageInfo4ES messageInfo = new MessageInfo4ES();
        if(project.getPageSize()==null || project.getPageNo()==null){
            return errorRet;
        }
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