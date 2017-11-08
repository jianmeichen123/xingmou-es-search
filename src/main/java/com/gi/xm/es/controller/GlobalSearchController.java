package com.gi.xm.es.controller;

import com.gi.xm.es.pojo.Query;
import com.gi.xm.es.service.*;
import com.gi.xm.es.view.MessageStatus;
import com.gi.xm.es.view.Result;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zcy on 17-11-6.
 */
@RestController
public class GlobalSearchController {

    @Autowired
    private Client client;

    @Autowired
    private InvestEventService investEventService;

    @Autowired
    InvestfirmsService investfirmsService;

    @Autowired
    private LaunchEventService launchEventService;

    @Autowired
    private MergeEventService mergeEventService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private ProjectService projectService;

    private static Result errorRet = new Result(MessageStatus.MISS_PARAMETER.getMessage(), MessageStatus.MISS_PARAMETER.getStatus());

    @RequestMapping(value="total",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result queryTotal(@RequestBody Query query) {
        Result ret = new Result();
        Map<String,Long> totalNumMap = new HashMap<String,Long>();
        Long newsNum = newsService.queryNum(query);
        Long projectNum = projectService.queryNum(query);
        Long investfirmsNum = investfirmsService.queryNum(query);
        Long investEventNum = investEventService.queryNum(query);
        Long mergeEventNum = mergeEventService.queryNum(query);
        Long launchEventNum = launchEventService.queryNum(query);
        totalNumMap.put("news",newsNum);
        totalNumMap.put("project",projectNum);
        totalNumMap.put("investfirms",investfirmsNum);
        totalNumMap.put("investEvent",investEventNum);
        totalNumMap.put("mergeEvent",mergeEventNum);
        totalNumMap.put("launchEvent",launchEventNum);
        ret.setNumMap(totalNumMap);
        return ret;
    }
}
