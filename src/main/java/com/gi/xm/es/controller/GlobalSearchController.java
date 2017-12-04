package com.gi.xm.es.controller;

import com.gi.xm.es.pojo.Query;
import com.gi.xm.es.service.*;
import com.gi.xm.es.view.MessageInfo4ES;
import com.gi.xm.es.view.MessageStatus;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

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

    private static MessageInfo4ES errorRet = new MessageInfo4ES(MessageStatus.MISS_PARAMETER.getStatus(),MessageStatus.MISS_PARAMETER.getMessage());

    @RequestMapping(value="total",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessageInfo4ES queryTotal(@RequestBody Query query) {
        MessageInfo4ES messageInfo = new MessageInfo4ES();
        LinkedHashMap<String,Long> totalNumMap = new LinkedHashMap<String,Long>();
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
        messageInfo.setNumMap(totalNumMap);
        Long totalHit = newsNum+projectNum+investfirmsNum+investEventNum+mergeEventNum+launchEventNum;
        messageInfo.setTotalhit(totalHit);
        return messageInfo;
    }
}
