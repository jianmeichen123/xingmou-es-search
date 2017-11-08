package com.gi.xm.es.service;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.pojo.Query;
import com.gi.xm.es.pojo.query.NewsQuery;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zcy on 17-11-4.
 */
@Service
public class NewsService extends BaseService {


    @Autowired
    private Client client;

    @Value("${ctdn.news.index}")
    private  String index;

    @Value("${ctdn.news.type}")
    private  String type;


    /**构建请求体:
     *全文搜索查符合条件并购事件数
     * @return
     */
    public  Long queryNum(Query query){
        NewsQuery newsQuery = new NewsQuery();
        newsQuery.setKeyword(query.getKeyword());
        SearchRequestBuilder qb = queryList(newsQuery);
        SearchHits ssh = getSearchHits(qb);
        return ssh.getTotalHits();
    }

    /**
     * 按多条件查询投资事件列表
     * @return
     */
    public SearchRequestBuilder queryList(NewsQuery newsQuery){
        SearchRequestBuilder srb = client.prepareSearch(index);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //按title
        if (!StringUtils.isEmpty(newsQuery.getKeyword())) {
            newsQuery.setKeyword(QueryParserBase.escape(newsQuery.getKeyword().trim()));
            BoolQueryBuilder shoudBuilder = QueryBuilders.boolQuery();
            shoudBuilder.should(queryBuilder.must(QueryBuilders.wildcardQuery("newsTitle", "*" + newsQuery.getKeyword() + "*")));
            shoudBuilder.should(queryBuilder.must(QueryBuilders.wildcardQuery("newsContent", "*" + newsQuery.getKeyword() + "*")));
            shoudBuilder.minimumNumberShouldMatch(1);
            queryBuilder.must(shoudBuilder);
        }

        //按新闻分类
        if(!StringUtils.isEmpty(newsQuery.getNewsTypeName())){
            queryBuilder.must(QueryBuilders.termQuery("newsTypeName", newsQuery.getNewsTypeName()));
        }

        //设置分页参数和请求参数
        srb.setQuery(queryBuilder);
        //求总数
        SearchResponse res = srb.setTypes(type).setSearchType(SearchType.DEFAULT).execute().actionGet();
        Long totalHit = res.getHits().totalHits();
        srb.addSort("newsReportTime", SortOrder.DESC);
        //设置分页参数和请求参数
        Integer tmp = newsQuery.getPageSize();
        Integer pageSize = newsQuery.getPageSize();
        Integer pageNo = newsQuery.getPageNo();
        if (pageSize*pageNo+pageSize > max_search_result){
            tmp =  max_search_result - pageSize*pageNo;
        }
        srb.setFrom(pageNo*pageSize).setSize(tmp);
        return srb;
    }

    /**
     * 查询结果转List
     * @param shs
     * @return
     * @throws Exception
     */
    public  List<Object> getResponseList (NewsQuery newsQuery, SearchHits shs)  throws Exception{
        List<Object> entityList = new ArrayList<>();
        for (SearchHit it : shs) {
            Map source = it.getSource();
            NewsQuery p = JSON.parseObject(JSON.toJSONString(source), NewsQuery.class);
            //获取对应的高亮域
            Map<String, HighlightField> result = it.highlightFields();
            if(result != null){
                //从设定的高亮域中取得指定域
                try{
                    for (Map.Entry<String, HighlightField> entry : result.entrySet()) {
                        String key = entry.getKey();
                        //获得高亮字段的原值
                        Field field = p.getClass().getDeclaredField(key);
                        field.setAccessible(true);
                        String value = field.get(p).toString();
                        //获得搜索关键字
                        String rep = "<comp>" + newsQuery.getNewsTitle()+ "</comp>";
                        //替换
                        field.set(p, value.replaceAll(newsQuery.getNewsTitle(), rep));
                    }
                } catch (Exception e) {
                    throw e;
                }
            }
            entityList.add(p);
        }
        return entityList;
    }

    /**
     * 返回查询命中
     * @param srb
     * @return
     */
    public SearchHits getSearchHits(SearchRequestBuilder srb){
        SearchResponse res = srb.setTypes(type).setSearchType(SearchType.DEFAULT).execute().actionGet();
        SearchHits shs = res.getHits();
        return shs;
    }
}
