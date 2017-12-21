package com.gi.xm.es.controller;

import com.gi.xm.es.pojo.Query;
import com.gi.xm.es.service.*;
import com.gi.xm.es.view.MessageInfo4ES;
import com.gi.xm.es.view.MessageStatus;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

    @Autowired
    private StartUpService startUpService;

    @Autowired
    private InvestorService investorService;

    @Autowired
    private ReportService reportService;
    private static MessageInfo4ES errorRet = new MessageInfo4ES(MessageStatus.MISS_PARAMETER.getStatus(),MessageStatus.MISS_PARAMETER.getMessage());

    @ApiOperation("查询项目,机构,投资事件,并购事件,上市事件,创业者,投资人,行业报告 每种列表总数")
    @ApiImplicitParam(paramType = "body", dataType = "Query", name = "query", value = "必填项:keyword", required = true)
    @RequestMapping(value="total",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessageInfo4ES queryTotal(@RequestBody Query query) {
        MessageInfo4ES messageInfo = new MessageInfo4ES();
       /* if(query.getKeyword() == null || StringUtils.isEmpty(query.getKeyword())){
            return errorRet;
        }*/
//        LinkedHashMap<String,Long> totalNumMap = new LinkedHashMap<String,Long>();
        Long newsNum = newsService.queryNum(query);
        Long projectNum = projectService.queryNum(query);
        Long investfirmsNum = investfirmsService.queryNum(query);
        Long investEventNum = investEventService.queryNum(query);
        Long mergeEventNum = mergeEventService.queryNum(query);
        Long launchEventNum = launchEventService.queryNum(query);
        Long startUpNum = startUpService.queryNum(query);
        Long investorNum = investorService.queryNum(query);
        Long reportNum=reportService.queryNum(query);
//        totalNumMap.put("news",newsNum);
//        totalNumMap.put("project",projectNum);
//        totalNumMap.put("investfirms",investfirmsNum);
//        totalNumMap.put("investEvent",investEventNum);
//        totalNumMap.put("mergeEvent",mergeEventNum);
//        totalNumMap.put("launchEvent",launchEventNum);
//        totalNumMap.put("startUp",startUpNum);
//        totalNumMap.put("investor",investorNum);

        // 汇总
        long event=investEventNum+mergeEventNum+launchEventNum;
        long person=startUpNum+investorNum;
        long all=event+person+newsNum+projectNum+investfirmsNum+reportNum;

        String [] orders=new String[]{"news","project","investfirms","investEvent","mergeEvent","launchEvent","startUp","investor","report"};
        String [] eventOrder=new String[]{"investEvent","mergeEvent","launchEvent"};
        String [] personOrder=new String[]{"startUp","investor"};

        LinkedHashMap<String,String> resultMap = new LinkedHashMap<String,String>();

        resultMap.put("news",String.valueOf(newsNum));
        resultMap.put("project",String.valueOf(projectNum));
        resultMap.put("investfirms",String.valueOf(investfirmsNum));
        resultMap.put("investEvent",String.valueOf(investEventNum));
        resultMap.put("mergeEvent",String.valueOf(mergeEventNum));
        resultMap.put("launchEvent",String.valueOf(launchEventNum));
        resultMap.put("startUp",String.valueOf(startUpNum));
        resultMap.put("investor",String.valueOf(investorNum));
        resultMap.put("event",String.valueOf(event));
        resultMap.put("person",String.valueOf(person));
        resultMap.put("all",String.valueOf(all));
        resultMap.put("report",String.valueOf(reportNum));

        String eventActive=getActiveTab(resultMap,eventOrder,"event");
        resultMap.put("event:active",eventActive);
        String personActive = getActiveTab(resultMap, personOrder, "person");
        resultMap.put("person:active",personActive);
        String allActive=getActiveTab(resultMap,orders,"all");

        String tab = allActive.split(":")[1];
        if(Arrays.asList(eventOrder).contains(tab)){
            resultMap.put("all:active","event:"+tab);
        }

        else if(Arrays.asList(personOrder).contains(tab)){
            resultMap.put("all:active","person:"+tab);
        }

        else{
            resultMap.put("all:active",tab+":"+tab);
        }

//        resultMap.put("all:active","project:project");

        /*System.out.println("event:"+event+",person:"+person+",project:"+projectNum+",news:"+newsNum);
        System.out.println("eventActive:"+eventActive+",personActive:"+personActive+",allActive:"+allActive);*/

        messageInfo.setResultMap(resultMap);
        Long totalHit = newsNum+projectNum+investfirmsNum+investEventNum+mergeEventNum+launchEventNum+startUpNum+investorNum+reportNum;
        messageInfo.setTotalhit(totalHit);
        return messageInfo;
    }

    private String getActiveTab(LinkedHashMap<String,String> resultMap,String [] orders,String activePrefix){
        if(resultMap==null||resultMap.size()==0||orders==null||orders.length==0|| StringUtils.isEmpty(activePrefix))
            return null;
        StringBuffer tab = new StringBuffer();
        if(Long.valueOf(resultMap.get(activePrefix))==0){
            tab.append(activePrefix).append(":").append(orders[0]);
            return tab.toString();
        }
        for(int i=0;i<orders.length;i++){
            if(Long.valueOf(resultMap.get(orders[i]))>0l){
                tab.append(activePrefix).append(":").append(orders[i]);
                break;
            }else{
                continue;
            }
        }
        return tab.toString();
    }
}