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
        JSONObject ctdn_project = JSONObject.parseObject("{ \"mappings\" : { \"project\": { \"properties\": { \"projectId\":{ \"type\": \"long\" }, \"code\":{ \"type\": \"keyword\" }, \"industryName\":{ \"type\": \"keyword\" }, \"industrySubName\":{ \"type\": \"keyword\" }, \"industryIds\":{ \"type\": \"keyword\" }, \"latestFinanceRound\":{ \"type\":\"keyword\" }, \"districtId\":{ \"type\": \"long\" }, \"districtSubId\":{ \"type\": \"long\" }, \"districtSubName\":{ \"type\": \"keyword\" }, \"logoSmall\":{ \"type\": \"keyword\" }, \"projTitle\":{ \"type\": \"keyword\" }, \"setupDT\": { \"type\": \"date\" }, \"latestFinanceDT\":{ \"type\": \"date\" }, \"latestFinanceAmountStr\":{ \"type\": \"keyword\" }, \"latestFinanceAmountNum\":{ \"type\": \"integer\" }, \"currencyTitle\":{ \"type\": \"keyword\" }, \"loadDate\":{ \"type\":\"long\" } } } } }");
        JSONObject ctdn_invest_event = JSONObject.parseObject("{ \"mappings\" : { \"invest_event\": { \"properties\": { \"eventId\":{ \"type\": \"long\" }, \"code\":{ \"type\": \"keyword\" }, \"sourceId\":{ \"type\": \"long\" }, \"sourceCode\":{ \"type\": \"keyword\" }, \"industryIds\":{ \"type\": \"keyword\" }, \"industryName\":{ \"type\": \"keyword\" }, \"industrySubName\":{ \"type\": \"keyword\" }, \"round\":{ \"type\":\"keyword\" }, \"districtId\":{ \"type\": \"long\" }, \"districtSubId\":{ \"type\": \"long\" }, \"districtSubName\":{ \"type\": \"keyword\" }, \"logo\":{ \"type\": \"keyword\" }, \"company\":{ \"type\": \"keyword\" }, \"investDate\": { \"type\": \"date\" }, \"amountStr\":{ \"type\":\"keyword\" }, \"amountNum\":{ \"type\":\"integer\" }, \"currencyType\":{ \"type\":\"keyword\" }, \"investSideJson\":{ \"type\":\"keyword\" }, \"bodyRole\":{ \"type\":\"keyword\" }, \"sourceType\":{ \"type\":\"keyword\" }, \"isClick\":{ \"type\":\"keyword\" } } } } }");
        JSONObject ctdn_merge_event = JSONObject.parseObject("{ \"mappings\" : { \"merge_event\": { \"properties\": { \"eventId\":{ \"type\": \"long\" }, \"code\":{ \"type\": \"keyword\" }, \"sourceId\":{ \"type\": \"long\" }, \"sourceCode\":{ \"type\": \"keyword\" }, \"industryIds\":{ \"type\": \"keyword\" }, \"industryName\":{ \"type\": \"keyword\" }, \"industrySubName\":{ \"type\": \"keyword\" }, \"districtSubName\":{ \"type\": \"keyword\" }, \"mergeType\":{ \"type\": \"keyword\" }, \"mergeState\":{ \"type\": \"keyword\" }, \"currencyType\":{ \"type\": \"keyword\" }, \"equityRate\":{ \"type\":\"integer\" }, \"equityrateRange\":{ \"type\":\"keyword\" }, \"mergeDate\":{ \"type\":\"date\" }, \"logo\":{ \"type\": \"keyword\" }, \"projTitle\":{ \"type\": \"keyword\" }, \"amountStr\":{ \"type\":\"keyword\" }, \"amountNum\":{ \"type\": \"integer\" }, \"mergeSideJson\":{ \"type\":\"keyword\" } } } } }");
        JSONObject ctdn_launch_event = JSONObject.parseObject("{ \"mappings\" : { \"launch_event\": { \"properties\": { \"id\":{ \"type\": \"long\" }, \"code\":{ \"type\": \"keyword\" }, \"sourceId\":{ \"type\": \"long\" }, \"sourceCode\":{ \"type\": \"keyword\" }, \"industryIds\":{ \"type\": \"keyword\" }, \"industryName\":{ \"type\": \"keyword\" }, \"industrySubName\":{ \"type\": \"keyword\" }, \"type\":{ \"type\": \"keyword\" }, \"stockExchange\":{ \"type\": \"keyword\" }, \"transferType\":{ \"type\": \"keyword\" }, \"marketLayer\":{ \"type\": \"keyword\" }, \"listedDate\":{ \"type\": \"date\" }, \"district\":{ \"type\": \"keyword\" }, \"logo\":{ \"type\": \"keyword\" }, \"company\":{ \"type\": \"keyword\" }, \"stockCode\": { \"type\": \"keyword\" }, \"bodyRole\":{ \"type\":\"keyword\" }, \"sourceType\":{ \"type\":\"keyword\" }, \"isClick\":{ \"type\":\"keyword\" } } } } }");

        ADDINDEX.put("ctdn_project",ctdn_project);
        ADDINDEX.put("ctdn_investfirms",ctdn_project);
        ADDINDEX.put("ctdn_invest_event",ctdn_invest_event);
       // ADDINDEX.put("ctdn_launch_event",ctdn_launch_event);
        ADDINDEX.put("ctdn_merge_event",ctdn_merge_event);
        //ADDINDEX.put("ctdn_quit_event",ctdn_quit_event);
    }
}
