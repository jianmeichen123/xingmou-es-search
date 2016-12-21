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
import net.sf.json.JSONObject;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.*;

@RestController
@RequestMapping(value = "/multisearch/")
public class MultiSearchController {

    private static final Logger LOG = LoggerFactory.getLogger(MultiSearchController.class);

    @Autowired
    private Client client;

    @Autowired
    private UserLogService userLogService;


    private static Result errorRet = new Result(MessageStatus.MISS_PARAMETER.getMessage(), MessageStatus.MISS_PARAMETER.getStatus());
    //图灵网站上的secret
    @Value("${xm.robot.secret}")
    private String[] secrets ;
    //图灵网站上的apiKey
    @Value("${xm.robot.apiKey}")
    private  String[] apiKeys ;

    /**
     * 根据关键字查询 @author zhangchunyuan
     *
     * @param query 搜索参数
     */
    @RequestMapping(value="globalSearch",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result searchByKey(String keys,@RequestHeader (name = "email" ,required = false) String email ,  @RequestBody Query query) {
        String keyword = query.getKeyword();
        if (keys == null && keyword == null) {
            return errorRet;
        }
        Result ret = new Result();
        UserSearchLog userSearchLog = null;
        if (email != null){
            userSearchLog = new UserSearchLog();
            userSearchLog.setEmail(email);
        }
        long startTime = System.currentTimeMillis();
        if (keys != null){
            //待加密的json数据
            int randNum = new Random().nextInt(3);
            TulingSend send = new TulingSend();
            send.setInfo(keys);
            send.setKey(apiKeys[1]);
            send.setUserid("1");
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
            if(category == null){
                category = "xm_project";
            }
            //构建各个索引的请求体
            SearchRequestBuilder projectsrb = queryProject(keyword,pageNo,pageSize);
            SearchRequestBuilder investfirmsrb = queryInvestFirm(keyword,pageNo,pageSize);
            SearchRequestBuilder investorsrb = queryInvestor(keyword,pageNo,pageSize);
            SearchRequestBuilder originatorsrb = queryOriginator(keyword,pageNo,pageSize);
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
            Map<String,Long> numHashMap = new HashMap<String,Long>();
            //遍历每个索引的命中结果
            for (MultiSearchResponse.Item item : multiSearchResponse.getResponses()) {
                SearchResponse response = item.getResponse();
                totalHit += response.getHits().totalHits();
                for (SearchHit searchHit : response.getHits()) {
                    String index = searchHit.getIndex();
                    if(index.equals(category)){
                        Map source = searchHit.getSource();

                        Object entity = JSONObject.toBean(JSONObject.fromObject(source),  EntityUtil.classHashMap.get(index));
                        //获取对应的高亮域
                        Map<String, HighlightField> result = searchHit.highlightFields();
                        //从设定的高亮域中取得指定域
                        for (Map.Entry<String, HighlightField> entry : result.entrySet()) {
                            String key = entry.getKey();
                            //取得定义的高亮标签
                            Text[] titleTexts =  entry.getValue().fragments();
                            //为title串值增加自定义的高亮标签
                            String value = "";
                            for(Text text : titleTexts){
                                value += text;
                            }
                            try {
                                Field field = entity.getClass().getDeclaredField(key);
                                field.setAccessible(true);
                                field.set(entity,value);
                            } catch (NoSuchFieldException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        dataList.add(entity);
                        totalCount = response.getHits().totalHits();
                    }
                    numHashMap.put(index,response.getHits().totalHits());
                }
            }
            Pagination page = new Pagination();
            page.setTotal(totalCount);
            page.setTotalhit(totalHit);
            page.setMap(numHashMap);
            page.setRecords(dataList);
            ret = new Result(MessageStatus.OK.getMessage(), MessageStatus.OK.getStatus(),page);
            if (userSearchLog != null){
                userSearchLog.setReturnjson(com.alibaba.fastjson.JSON.toJSONString(ret));
                userSearchLog.setTxt(query.getKeyword());
                userSearchLog.setType(1);
                userSearchLog.setPageNo(pageNo);
            }
        }
        long endTime = System.currentTimeMillis();
        if (userSearchLog != null){
            userSearchLog.setLoadtime(endTime-startTime);
            userLogService.addUserSearchLog(userSearchLog);
            userSearchLog.setReturntime(endTime);
            userSearchLog.setSendtime(startTime);
        }
        return ret;
    }


    private SearchRequestBuilder queryProject(String keyword , int pageNo,int pageSize){
        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.field("description");
        highlightBuilder.preTags("<span class = 'highlight'>");
        highlightBuilder.postTags("</span>");
        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.matchQuery("title", keyword).analyzer("ik_max_word"))
                .add(QueryBuilders.matchQuery("description", keyword).analyzer("ik_max_word"))
                .add(QueryBuilders.termQuery("lables", keyword))
                .add(QueryBuilders.termQuery("indudstryName", keyword))
                .add(QueryBuilders.termQuery("indudstrySubName", keyword))
                .add(QueryBuilders.termQuery("roundName", keyword));
        SearchRequestBuilder srb = getRequestBuilder(qb,highlightBuilder,EntityUtil.PROJECT_INDEX_A,pageNo,pageSize);
        return srb;
    }

    private SearchRequestBuilder queryInvestFirm(String keyword, int pageNo,int pageSize){
        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.field("desciption");
        highlightBuilder.preTags("<span class = 'highlight'>");
        highlightBuilder.postTags("</span>");

        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("name", keyword))
                .add(QueryBuilders.matchQuery("description", keyword).analyzer("ik_max_word"))
                .add(QueryBuilders.termQuery("areaNames", keyword))
                .add(QueryBuilders.termQuery("roundNames", keyword));
        SearchRequestBuilder srb = getRequestBuilder(qb,highlightBuilder,EntityUtil.INVESTFIRM_INDEX_A,pageNo,pageSize);
        return srb;
    }

    private SearchRequestBuilder queryInvestor(String keyword,  int pageNo,int pageSize){
        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.field("investfirmName");
        highlightBuilder.field("desciption");
        highlightBuilder.preTags("<span class = 'highlight'>");
        highlightBuilder.postTags("</span>");

        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("name", keyword))
                .add(QueryBuilders.matchQuery("description", keyword).analyzer("ik_max_word"))
                .add(QueryBuilders.termQuery("investfirmName", keyword));
        SearchRequestBuilder srb = getRequestBuilder(qb,highlightBuilder,EntityUtil.INVESTOR_INDEX_A,pageNo,pageSize);
        return srb;
    }
    private SearchRequestBuilder queryOriginator(String keyword, int pageNo,int pageSize){
        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.field("jobDescription");
        highlightBuilder.field("projectName");
        highlightBuilder.preTags("<span class = 'highlight'>");
        highlightBuilder.postTags("</sapn>");
        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("name", keyword))
                .add(QueryBuilders.termQuery("projectName", keyword))
                .add(QueryBuilders.matchQuery("jobDescription", keyword).analyzer("ik_max_word"));
        SearchRequestBuilder srb = getRequestBuilder(qb,highlightBuilder,EntityUtil.ORIGINATOR_INDEX_A,pageNo,pageSize);
        return srb;
    }

    private SearchRequestBuilder getRequestBuilder(QueryBuilder queryBuilder ,HighlightBuilder highlightBuilder, String index, int pageNo,int pageSize){

        SearchRequestBuilder srb = client.prepareSearch(index);
        srb.setQuery(queryBuilder);
        srb.highlighter(highlightBuilder);
        srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        pageNo = (pageNo>20) ? 20 : pageNo;
        srb.setFrom(pageNo * pageSize).setSize(pageSize);
        return srb;
    }
}