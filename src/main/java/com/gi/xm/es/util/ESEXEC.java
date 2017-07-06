package com.gi.xm.es.util;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vincent on 17-4-28.
 */
public  class  ESEXEC {

    public static Map<String,JSONObject>  ADDINDEX = new HashMap<>();
    static {
        JSONObject ctdn_project = JSONObject.parseObject("{ \"mappings\" : { \"project\": { \"properties\": { \"id\":{ \"type\": \"long\" }, \"code\":{ \"type\": \"keyword\" }, \"industryName\":{ \"type\": \"keyword\" }, \"industrySubName\":{ \"type\": \"keyword\" }, \"industryIds\":{ \"type\": \"keyword\" }, \"latestFinanceRound\":{ \"type\":\"keyword\" }, \"districtId\":{ \"type\": \"long\" }, \"districtSubId\":{ \"type\": \"long\" }, \"districtSubName\":{ \"type\": \"keyword\" }, \"logoSmall\":{ \"type\": \"keyword\" }, \"projTitle\":{ \"type\": \"keyword\" }, \"setupDT\": { \"type\": \"date\" }, \"latestFinanceDT\":{ \"type\": \"date\" }, \"latestFinanceAmountStr\":{ \"type\": \"keyword\" }, \"latestFinanceAmountNum\":{ \"type\": \"integer\" }, \"currencyType\":{ \"type\": \"keyword\" }, \"loadDate\":{ \"type\":\"long\" } } } } }");
        JSONObject ctdn_invest_event = JSONObject.parseObject("{ \"mappings\" : { \"invest_event\": { \"properties\": { \"eventId\":{ \"type\": \"long\" }, \"sourceId\":{ \"type\": \"long\" }, \"sourceCode\":{ \"type\": \"keyword\" }, \"industryIds\":{ \"type\": \"keyword\" }, \"industryName\":{ \"type\": \"keyword\" }, \"industrySubName\":{ \"type\": \"keyword\" }, \"round\":{ \"type\":\"keyword\" }, \"districtId\":{ \"type\": \"long\" }, \"districtSubId\":{ \"type\": \"long\" }, \"districtSubName\":{ \"type\": \"keyword\" }, \"logo\":{ \"type\": \"keyword\" }, \"company\":{ \"type\": \"keyword\" }, \"investDate\": { \"type\": \"date\" }, \"amountStr\":{ \"type\":\"keyword\" }, \"amountNum\":{ \"type\":\"integer\" }, \"currencyType\":{ \"type\":\"keyword\" }, \"investSideJson\":{ \"type\":\"nested\" ,\"properties\": { \"code\": { \"type\": \"keyword\" }, \"id\": { \"type\": \"integer\" }, \"invstor\": { \"type\": \"keyword\" }, \"isClick\": { \"type\": \"short\" }, \"isLeader\": { \"type\": \"short\" }, \"type\":{\"type\":\"keyword\"} }}, \"bodyRole\":{ \"type\":\"keyword\" }, \"sourceType\":{ \"type\":\"keyword\" }, \"isClick\":{ \"type\":\"keyword\" } } } } }");
        JSONObject ctdn_merge_event = JSONObject.parseObject("{ \"mappings\" : { \"merge_event\": { \"properties\": { \"eventId\":{ \"type\": \"long\" },  \"sourceId\":{ \"type\": \"long\" }, \"sourceCode\":{ \"type\": \"keyword\" }, \"industryIds\":{ \"type\": \"keyword\" }, \"industryName\":{ \"type\": \"keyword\" }, \"industrySubName\":{ \"type\": \"keyword\" }, \"districtSubName\":{ \"type\": \"keyword\" }, \"mergeType\":{ \"type\": \"keyword\" }, \"currencyType\":{ \"type\": \"keyword\" }, \"equityRate\":{ \"type\":\"integer\" }, \"equityrateRange\":{ \"type\":\"keyword\" }, \"mergeDate\":{ \"type\":\"date\" }, \"logo\":{ \"type\": \"keyword\" }, \"projTitle\":{ \"type\": \"keyword\" }, \"amountStr\":{ \"type\":\"keyword\" }, \"amountNum\":{ \"type\": \"integer\" }, \"mergeSideJson\":{ \"type\":\"nested\",\"properties\": { \"type\": { \"type\": \"keyword\" }, \"code\": { \"type\": \"keyword\" }, \"id\": { \"type\": \"integer\" }, \"title\": { \"type\": \"keyword\" }, \"isClick\": { \"type\": \"short\" } } } } } } }");
        JSONObject ctdn_launch_event = JSONObject.parseObject("{ \"mappings\" : { \"launch_event\": { \"properties\": { \"eventId\":{ \"type\": \"long\" }, \"sourceId\":{ \"type\": \"long\" }, \"sourceCode\":{ \"type\": \"keyword\" }, \"industryIds\":{ \"type\": \"keyword\" }, \"industryName\":{ \"type\": \"keyword\" }, \"industrySubName\":{ \"type\": \"keyword\" }, \"type\":{ \"type\": \"keyword\" }, \"typeSub\":{ \"type\": \"keyword\" }, \"listedDate\":{ \"type\": \"date\" }, \"districtSubName\":{ \"type\": \"keyword\" }, \"logoSmall\":{ \"type\": \"keyword\" }, \"projTitle\":{ \"type\": \"keyword\" }, \"stockCode\": { \"type\": \"keyword\" } } } } }");
        JSONObject ctdn_investfirms = JSONObject.parseObject("{ \"mappings\" : { \"investfirms\": { \"properties\": { \"orgId\":{ \"type\": \"long\" },\"investRounds\":{ \"type\": \"keyword\" }, \"industryIds\":{ \"type\": \"keyword\" },  \"investStage\":{ \"type\": \"keyword\" }, \"orgType\":{ \"type\": \"keyword\" }, \"districtId\":{ \"type\": \"long\" }, \"districtSubId\":{ \"type\": \"long\" }, \"capitalType\":{ \"type\":\"keyword\" }, \"currencyType\":{ \"type\": \"keyword\" }, \"logoSmall\":{ \"type\": \"keyword\" }, \"investOrg\":{ \"type\": \"keyword\" }, \"investTotal\":{ \"type\": \"integer\" }, \"totalRatio\":{ \"type\": \"integer\" }, \"investAmountNum\":{ \"type\": \"long\" }, \"investAmountStr\":{ \"type\": \"keyword\" }, \"amountRatio\":{ \"type\": \"integer\" }, \"investProjJson\":{ \"type\": \"keyword\"}, \"newestInvestDate\":{ \"type\": \"date\" } } } } }");
        ADDINDEX.put("ctdn_project",ctdn_project);
        ADDINDEX.put("ctdn_investfirms",ctdn_investfirms);
        ADDINDEX.put("ctdn_invest_event",ctdn_invest_event);
        ADDINDEX.put("ctdn_launch_event",ctdn_launch_event);
        ADDINDEX.put("ctdn_merge_event",ctdn_merge_event);
    }
}
