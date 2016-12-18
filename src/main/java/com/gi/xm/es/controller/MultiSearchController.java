package com.gi.xm.es.controller;

import com.gi.xm.es.pojo.Query;
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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/multisearch/")
public class MultiSearchController {

    private static final Logger LOG = LoggerFactory.getLogger(MultiSearchController.class);

    @Autowired
    private Client client;

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
    public Result searchByKey(String keys, @RequestBody Query query) {
        Result ret = new Result();
        if ( query.getKeyword() == null &&keys ==null) {
            ret = new Result(MessageStatus.MISS_PARAMETER.getMessage(), MessageStatus.MISS_PARAMETER.getStatus());
            return ret;
        }

        if (keys != null){
            //待加密的json数据
            int randNum = new Random().nextInt(3);
            String data = "{\"key\":\"" + apiKeys[randNum] + "\",\"info\":\"" + keys + "\"}";
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
            return ret;
        }

        String keyword = query.getKeyword();
        String category = query.getCategory();
        Integer pageSize = query.getPageSize();
        Integer pageNo = query.getPageNo();
        if(category == null){
            category = "xm_project";
        }

        long startTime = System.currentTimeMillis();

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
        Result result = new Result(MessageStatus.OK.getMessage(), MessageStatus.OK.getStatus(),page);

        long endTime = System.currentTimeMillis();
        System.out.print("查询用时："+(endTime-startTime)+" ms");
        LOG.info(pageNo + ":" + query.getKeyword());
        return result;
    }


    private SearchRequestBuilder queryProject(String keyword , int pageNo,int pageSize){
        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.matchQuery("title", keyword).analyzer("ik_max_word"))
                .add(QueryBuilders.matchQuery("description", keyword).analyzer("ik_max_word"))
                .add(QueryBuilders.termQuery("lables", keyword))
                .add(QueryBuilders.termQuery("indudstryName", keyword))
                .add(QueryBuilders.termQuery("indudstrySubName", keyword))
                .add(QueryBuilders.termQuery("roundName", keyword));
        SearchRequestBuilder srb = getRequestBuilder(qb,EntityUtil.PROJECT_INDEX_A,pageNo,pageSize);
        return srb;
    }

    private SearchRequestBuilder queryInvestFirm(String keyword, int pageNo,int pageSize){
        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("name", keyword))
                .add(QueryBuilders.matchQuery("description", keyword).analyzer("ik_max_word"))
                .add(QueryBuilders.termQuery("areaNames", keyword))
                .add(QueryBuilders.termQuery("roundNames", keyword));
        SearchRequestBuilder srb = getRequestBuilder(qb,EntityUtil.INVESTFIRM_INDEX_A,pageNo,pageSize);
        return srb;
    }

    private SearchRequestBuilder queryInvestor(String keyword,  int pageNo,int pageSize){
        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("name", keyword))
                .add(QueryBuilders.matchQuery("description", keyword).analyzer("ik_max_word"))
                .add(QueryBuilders.termQuery("investfirmName", keyword));
        SearchRequestBuilder srb = getRequestBuilder(qb,EntityUtil.INVESTOR_INDEX_A,pageNo,pageSize);
        return srb;
    }
    private SearchRequestBuilder queryOriginator(String keyword, int pageNo,int pageSize){
        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("name", keyword))
                .add(QueryBuilders.termQuery("projectName", keyword))
                .add(QueryBuilders.matchQuery("jobDescription", keyword).analyzer("ik_max_word"));
        SearchRequestBuilder srb = getRequestBuilder(qb,EntityUtil.ORIGINATOR_INDEX_A,pageNo,pageSize);
        return srb;
    }

    private SearchRequestBuilder getRequestBuilder(QueryBuilder queryBuilder ,String index, int pageNo,int pageSize){
        SearchRequestBuilder srb = client.prepareSearch(index);
        srb.setQuery(queryBuilder);
        //srb.setScroll(new TimeValue(60000));
        srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        srb.setFrom(pageNo * pageSize).setSize(pageSize);
        return srb;
    }
}