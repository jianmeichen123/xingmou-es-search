package com.gi.xm.es.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vincent on 17-4-28.
 */
public  class  ESEXEC {

    public static Map<String,String>  ADDINDEX = new HashMap<>();

    static {


        ADDINDEX.put("invest_event","curl -XPUT 10.9.130.135:9200/ctdn_invest_event -d '{ 'mappings' : { 'invest_event': { 'properties': { 'id':{ 'type': 'long' }, 'code':{ 'type': 'keyword' }, 'sourceId':{ 'type': 'long' }, 'sourceCode':{ 'type': 'keyword' }, 'industryIds':{ 'type': 'keyword' }, 'industryName':{ 'type': 'keyword' }, 'industrySubName':{ 'type': 'keyword' }, 'round':{ 'type':'keyword' }, 'districtId':{ 'type': 'long' }, 'districtSubId':{ 'type': 'long' }, 'district':{ 'type': 'keyword' }, 'logo':{ 'type': 'keyword' }, 'company':{ 'type': 'keyword' }, 'investdate': { 'type': 'date' }, 'amountStr':{ 'type':'keyword' }, 'amountNum':{ 'type':'integer' }, 'currencyTitle':{ 'type':'keyword' }, 'investSideJson':{ 'type':'keyword' }, 'bodyRole':{ 'type':'keyword' }, 'sourceType':{ 'type':'keyword' }, 'isClick':{ 'type':'keyword' } } } } }'");

    }
}
