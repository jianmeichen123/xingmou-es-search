package com.gi.xm.es.controller;

import com.gi.xm.es.pojo.Query;
import com.gi.xm.es.util.EntityUtil;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/multisearch/")
public class MultiSearchController {

    private static final Logger LOG = LoggerFactory.getLogger(MultiSearchController.class);

    @Autowired
    private Client client;


    /**
     * 根据关键字查询 @author zhangchunyuan
     *
     * @param query 搜索参数
     */
    @RequestMapping(value="globalSearch",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result searchByKey(@RequestBody Query query) throws ClassNotFoundException {
        String keyword = query.getKeyword();
        String category = query.getCategory();
        Integer pageSize = query.getPageSize();
        Integer pageNo = query.getPageNo();
        if (query == null || keyword == null) {
            Result ret = new Result(MessageStatus.MISS_PARAMETER.getMessage(), MessageStatus.MISS_PARAMETER.getStatus());
            return ret;
        }
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
        Result result = new Result(MessageStatus.OK.getMessage(), MessageStatus.OK.getStatus(),page);

        long endTime = System.currentTimeMillis();
        System.out.print("查询用时："+(endTime-startTime)+" ms");
        LOG.info(pageNo + ":" + query.getKeyword());
        return result;
    }


    private SearchRequestBuilder queryProject(String keyword , int pageNo,int pageSize){
        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.field("description");
        highlightBuilder.preTags("<span class = 'highlight'>");
        highlightBuilder.postTags("</span>");
        QueryBuilder qb = QueryBuilders.disMaxQuery()
                .add(QueryBuilders.termQuery("title",keyword))
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
        srb.setFrom(pageNo * pageSize).setSize(pageSize);
        return srb;
    }
}