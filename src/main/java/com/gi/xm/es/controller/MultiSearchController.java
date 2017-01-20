package com.gi.xm.es.controller;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.pojo.Query;
import com.gi.xm.es.pojo.TulingSend;
import com.gi.xm.es.pojo.UserSearchLog;
import com.gi.xm.es.service.UserLogService;
import com.gi.xm.es.util.Aes;
import com.gi.xm.es.util.EntityUtil;
import com.gi.xm.es.util.Md5;
import com.gi.xm.es.util.PostServer;
import com.gi.xm.es.view.MessageStatus;
import com.gi.xm.es.view.Pagination;
import com.gi.xm.es.view.Result;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.*;

@RestController
@RequestMapping(value = "/multisearch/")
public class MultiSearchController {

    private static final Logger LOG = LoggerFactory.getLogger(MultiSearchController.class);

    @Autowired
    private Client client;

    @Autowired
    private UserLogService userLogService;


    private static List<Object> emtpyList  = new ArrayList<Object>();


    private static Result errorRet = new Result(MessageStatus.MISS_PARAMETER.getMessage(), MessageStatus.MISS_PARAMETER.getStatus());
    //图灵网站上的secret
    @Value("${xm.robot.secret}")
    private String[] secrets ;
    //图灵网站上的apiKey
    @Value("${xm.robot.apiKey}")
    private  String[] apiKeys ;

    @Value("${xm.search.env}")
    private  String env ;


    /**
     * 根据关键字查询 @author zhangchunyuan
     *@param keys:图灵接口传入的关键字
     *@param from:搜索来源（机器人 or 全站搜索）
     *@param userInfo:搜索发起用户相关信息
     *@param query:全站搜索相关 （关键字,分类,分页参数）
     *
     */
    @RequestMapping(value="globalSearch",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result searchByKey(String keys,String from,@RequestHeader (name = "userInfo" ,required = false) String userInfo ,  @RequestBody Query query) {
        String keyword = query.getKeyword();
        if (keys == null && keyword == null) {
            return errorRet;
        }
        Result ret = new Result();
        UserSearchLog userSearchLog = null;
        if (userInfo != null){
            try {
                userSearchLog = JSON.parseObject(URLDecoder.decode(userInfo,"UTF-8"), UserSearchLog.class);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        long startTime = System.currentTimeMillis();
        if (keys != null){
            //待加密的json数据`
            int randNum = new Random().nextInt(3);
            TulingSend send = new TulingSend();
            send.setInfo(keys);
            int keynum = userSearchLog!=null&&userSearchLog!=null?userSearchLog.getRoleId()%3:0;
            send.setKey(apiKeys[keynum]);
            if (userSearchLog!=null&&userSearchLog.getEmail()!=null){
                StringBuffer sb=new StringBuffer(userSearchLog.getEmail());
                sb=sb.reverse();
                send.setUserid(Md5.MD5(sb.toString()));
            }
            String data = JSON.toJSONString(send);
            //获取时间戳
            String timestamp = String.valueOf(System.currentTimeMillis());

            //生成密钥
            String keyParam = secrets[randNum] + timestamp + apiKeys[randNum];
            String key = Md5.MD5(keyParam);

            //加密
            Aes mc = new Aes(key);
            data = mc.encrypt(data);

            //封装请求参数
            com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
            json.put("key", apiKeys[randNum]);
            json.put("timestamp", timestamp);
            json.put("data", data);
            //请求图灵api
            String str = PostServer.SendPost(json.toString(), "http://www.tuling123.com/openapi/api");
            ret.setMsg(str);
            if (userSearchLog != null){
                userSearchLog.setPageNo(0);
                userSearchLog.setTxt(keys);
                userSearchLog.setType(0);
                userSearchLog.setReturnjson(str);
            }
        }else {
            String category = query.getCategory();
            Integer pageSize = query.getPageSize();
            Integer pageNo = query.getPageNo();
            //构建各个索引的请求体 先查询每个分类的个数
            SearchRequestBuilder projectsrb = queryProject(keyword, null, null);
            SearchRequestBuilder investfirmsrb = queryInvestFirm(keyword, null, null);
            SearchRequestBuilder investorsrb = queryInvestor(keyword, null, null);
            SearchRequestBuilder originatorsrb = queryOriginator(keyword, null, null);

            MultiSearchResponse multiSearchResponse = client.prepareMultiSearch()
                    .add(projectsrb)
                    .add(investfirmsrb)
                    .add(investorsrb)
                    .add(originatorsrb)
                    .execute().actionGet();
            //命中总数
            Long totalHit = 0l;
            //单个分类总条数
            Long totalCount = 0l;
            List<Object> dataList = new ArrayList<Object>();
            LinkedHashMap<String, Long> numHashMap = new LinkedHashMap<String, Long>();
            //遍历每个索引的命中结果
            for (MultiSearchResponse.Item item : multiSearchResponse.getResponses()) {
                SearchResponse response = item.getResponse();
                totalHit += response.getHits().totalHits();
                for (SearchHit searchHit : response.getHits()) {
                    String index = searchHit.getIndex();
                    numHashMap.put(index, response.getHits().totalHits());
                }
            }

            if (numHashMap.size() > 0) {
                SearchRequestBuilder srb = null;
                String selectIndex = null;
                //如果category不为空,则获得对应的SearchRequestBuilder
                if (category != null) {
                    selectIndex = category;
                    switch (category) {
                        case "xm_project": {
                            srb = queryProject(keyword, pageNo, pageSize);
                            break;
                        }
                        case "xm_investfirm": {
                            srb = queryInvestFirm(keyword, pageNo, pageSize);
                            break;
                        }
                        case "xm_originator": {
                            srb = queryOriginator(keyword, pageNo, pageSize);
                            break;
                        }
                        case "xm_investor": {
                            srb = queryInvestor(keyword, pageNo, pageSize);
                            break;
                        }
                    }
                } else {
                    //category为空,则查寻numHashMap中的第一个有记录的分类
                    for (String index : numHashMap.keySet()) {
                        if (numHashMap.get(index) != 0) {
                            selectIndex = index;
                            switch (index) {
                                case "xm_project": {
                                    srb = queryProject(keyword, pageNo, pageSize);
                                    break;
                                }
                                case "xm_investfirm": {
                                    srb = queryInvestFirm(keyword, pageNo, pageSize);
                                    break;
                                }
                                case "xm_originator": {
                                    srb = queryOriginator(keyword, pageNo, pageSize);
                                    break;
                                }
                                case "xm_investor": {
                                    srb = queryInvestor(keyword, pageNo, pageSize);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
                //执行查询请求
                SearchResponse response = srb.execute().actionGet();
                totalCount = response.getHits().totalHits();
                SearchHits searchHits = response.getHits();
                SearchHit[] hits = searchHits.getHits();
                for (SearchHit searchHit : response.getHits()) {
                    Map source = searchHit.getSource();
                    source.put("category",selectIndex);
                    Object entity = JSON.parseObject(JSON.toJSONString(source), EntityUtil.classHashMap.get(selectIndex));
                    //获取对应的高亮域
                    Map<String, HighlightField> result = searchHit.highlightFields();
                    //从设定的高亮域中取得指定域
                    for (Map.Entry<String, HighlightField> entry : result.entrySet()) {
                        String key = entry.getKey();
                        //取得定义的高亮标签
                        Text[] titleTexts = entry.getValue().fragments();
                        //为title串值增加自定义的高亮标签
                        String value = "";
                        for (Text text : titleTexts) {
                            value += text;
                        }
                        try {
                            Field field = entity.getClass().getDeclaredField(key);
                            field.setAccessible(true);
                            field.set(entity, value);
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    dataList.add(entity);
                }
                Pagination page = new Pagination();
                page.setTotal(totalCount);
                page.setTotalhit(totalHit);
                page.setMap(numHashMap);
                page.setRecords(dataList);
                page.setMatchIndex(selectIndex);
                ret = new Result(MessageStatus.OK.getMessage(), MessageStatus.OK.getStatus(), page);
                if (userSearchLog != null) {
                    userSearchLog.setReturnjson(com.alibaba.fastjson.JSON.toJSONString(ret));
                    userSearchLog.setTxt(query.getKeyword());
                    userSearchLog.setType(1);
                    userSearchLog.setPageNo(pageNo);
                }
            }else{
                Pagination page = new Pagination();
                page.setTotal(0l);
                page.setTotalhit(0l);
                page.setMap(null);
                page.setRecords(emtpyList);
                page.setMatchIndex(null);
                ret = new Result(MessageStatus.OK.getMessage(), MessageStatus.OK.getStatus(),page);
            }
        }
        long endTime = System.currentTimeMillis();
        if (userSearchLog != null){
            userSearchLog.setLoadtime(endTime-startTime);
            userSearchLog.setReturntime(endTime);
            userSearchLog.setSendtime(startTime);
            userSearchLog.setFrom(from==null?"search":"robot");
            userSearchLog.setEnv(env);
            userLogService.addUserSearchLog(userSearchLog);
        }
        return ret;
    }



    private SearchRequestBuilder queryProject(String keyword , Integer pageNo,Integer pageSize){
        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.field("description");
        highlightBuilder.preTags("<span class = 'highlight'>");
        highlightBuilder.postTags("</span>");
        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("title",keyword))
                .add(QueryBuilders.matchQuery("description", keyword).analyzer("ik_smart"))
                .add(QueryBuilders.termQuery("lables", keyword));
                /*.add(QueryBuilders.termQuery("indudstryName", keyword))
                .add(QueryBuilders.termQuery("indudstrySubName", keyword))
                .add(QueryBuilders.termQuery("roundName", keyword));*/
        SearchRequestBuilder srb = getRequestBuilder(qb,highlightBuilder,EntityUtil.PROJECT_INDEX_A,pageNo,pageSize);
        return srb;
    }

    private SearchRequestBuilder queryInvestFirm(String keyword, Integer pageNo,Integer pageSize){
        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.field("description");
        highlightBuilder.preTags("<span class = 'highlight'>");
        highlightBuilder.postTags("</span>");

        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("name", keyword))
                .add(QueryBuilders.matchQuery("description", keyword).analyzer("ik_smart"))
                .add(QueryBuilders.termQuery("areaNames", keyword))
                .add(QueryBuilders.termQuery("roundNames", keyword));
        SearchRequestBuilder srb = getRequestBuilder(qb,highlightBuilder,EntityUtil.INVESTFIRM_INDEX_A,pageNo,pageSize);
        return srb;
    }

    private SearchRequestBuilder queryInvestor(String keyword,  Integer pageNo,Integer pageSize){
        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.field("investfirmName");
        highlightBuilder.field("description");
        highlightBuilder.preTags("<span class = 'highlight'>");
        highlightBuilder.postTags("</span>");

        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("name", keyword))
                .add(QueryBuilders.matchQuery("description", keyword).analyzer("ik_smart"))
                .add(QueryBuilders.termQuery("investfirmName", keyword));
        SearchRequestBuilder srb = getRequestBuilder(qb,highlightBuilder,EntityUtil.INVESTOR_INDEX_A,pageNo,pageSize);
        return srb;
    }
    private SearchRequestBuilder queryOriginator(String keyword, Integer pageNo,Integer pageSize){
        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.field("jobDescription");
        highlightBuilder.field("projectName");
        highlightBuilder.preTags("<span class = 'highlight'>");
        highlightBuilder.postTags("</span>");
        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("name", keyword))
                .add(QueryBuilders.termQuery("projectName", keyword))
                .add(QueryBuilders.matchQuery("jobDescription", keyword).analyzer("ik_smart"));
        SearchRequestBuilder srb = getRequestBuilder(qb,highlightBuilder,EntityUtil.ORIGINATOR_INDEX_A,pageNo,pageSize);
        return srb;
    }

    private SearchRequestBuilder getRequestBuilder(QueryBuilder queryBuilder ,HighlightBuilder highlightBuilder, String index, Integer pageNo,Integer pageSize){

        SearchRequestBuilder srb = client.prepareSearch(index);
        srb.setQuery(queryBuilder);
        srb.highlighter(highlightBuilder);
        srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        if(pageNo != null && pageSize != null){
            srb.setFrom(pageNo * pageSize).setSize(pageSize);
        }
        return srb;
    }

}