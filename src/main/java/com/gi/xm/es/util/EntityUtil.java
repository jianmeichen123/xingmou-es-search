package com.gi.xm.es.util;


import com.gi.xm.es.pojo.Investfirms;
import com.gi.xm.es.pojo.Investor;
import com.gi.xm.es.pojo.Originator;
import com.gi.xm.es.pojo.Project;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zcy on 16-12-8.
 */
public  class  EntityUtil{

    public static final String PROJECT_INDEX = "xm_project" ;
    public static final String INVESTFIRM_INDEX = "xm_investfirm" ;
    public static final String INVESTOR_INDEX = "xm_investor" ;
    public static final String ORIGINATOR_INDEX = "xm_originator" ;

    public static final String PROJECT_INDEX_A = "xm_project_a" ;
    public static final String INVESTFIRM_INDEX_A = "xm_investfirm_a" ;
    public static final String INVESTOR_INDEX_A = "xm_investor_a" ;
    public static final String ORIGINATOR_INDEX_A = "xm_originator_a" ;

    public static Map<String,Class> classHashMap = new HashMap<String,Class>();
    static {
        classHashMap.put(PROJECT_INDEX,Project.class);
        classHashMap.put(INVESTFIRM_INDEX,Investfirms.class);
        classHashMap.put(INVESTOR_INDEX,Investor.class);
        classHashMap.put(ORIGINATOR_INDEX,Originator.class);
    }
}
