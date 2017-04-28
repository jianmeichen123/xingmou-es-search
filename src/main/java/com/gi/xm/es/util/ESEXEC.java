package com.gi.xm.es.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vincent on 17-4-28.
 */
public  class  ESEXEC {

    public static Map<String,String>  ADDINDEX = new HashMap<>();

    static {
        ADDINDEX.put("xm_project_a","curl -XPUT 10.9.130.135:9200/xm_project_a -d '{\"mappings\" : {\"project\": {\"properties\": {\"indudstrySubName\": {\"analyzer\": \"ik_smart\",\"type\": \"text\"},\"indudstryName\": {\"analyzer\": \"ik_smart\",\"type\": \"text\"},\"icon\": {\"type\": \"text\"},\"description\": {\"analyzer\": \"ik_max_word\",\"type\": \"text\"},\"logo\": {\"type\": \"text\"},\"roundName\": {\"analyzer\": \"ik_smart\",\"type\": \"text\"},\"title\": {\"analyzer\": \"ik_max_word\",\"type\": \"text\"},\"lables\": {\"analyzer\": \"ik_max_word\",\"type\": \"text\"},\"createDate\": {\"type\": \"text\"},\"sid\": {\"type\": \"text\"}}}}}'");
    }
}
