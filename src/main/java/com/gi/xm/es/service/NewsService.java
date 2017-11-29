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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            queryBuilder.must(QueryBuilders.termQuery("title",newsQuery.getKeyword().toLowerCase()));
        }

        //按新闻分类
        if(!StringUtils.isEmpty(newsQuery.getTypeId())){
            queryBuilder.must(QueryBuilders.termQuery("typeId", newsQuery.getTypeId()));
        }

        //按code
//        if(!StringUtils.isEmpty(newsQuery.getCode())){
//            queryBuilder.must(QueryBuilders.termQuery("code",newsQuery.getCode()));
//        }

        //设置分页参数和请求参数
        srb.setQuery(queryBuilder);
        srb.addSort("orderTime", SortOrder.DESC);
        srb.addSort("_score", SortOrder.DESC);

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
                if(!StringUtils.isEmpty(newsQuery.getKeyword())){
                    Field title = p.getClass().getDeclaredField("title");
                    title.setAccessible(true);
                    String titleVal = title.get(p).toString();
                    //获得搜索关键字
//                    String titleHtml = "<comp>" + newsQuery.getKeyword()+ "</comp>";
//                    title.set(p, titleVal.replaceAll("(?i)"+newsQuery.getKeyword(), titleHtml));


                    title.set(p,ignoreCase(titleVal,newsQuery.getKeyword()));
                    Field overview = p.getClass().getDeclaredField("overview");
                    overview.setAccessible(true);
                    String overviewVal = overview.get(p).toString();
                    //获得搜索关键字
                    //String overviewHtml = "<comp>" + newsQuery.getKeyword()+ "</comp>";
                    //overview.set(p, overviewVal.replaceAll("(?i)"+newsQuery.getKeyword().toUpperCase(), overviewHtml));

                    overview.set(p,ignoreCase(overviewVal,newsQuery.getKeyword()));
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

    private String ignoreCase(String str,String regex) {
        //保存你要添加的html代码的长度
        int len = 0;
        String s = "<comp></comp>";
        //不区分大小写匹配字符串
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        //循环查找，可能匹配到的不止一个字符串
        while (matcher.find()) {
        //截取字符串，临时保存匹配到的字符串
        //起始位置和结束位置都要加一个len长度
        String match = str.substring(matcher.start() + len, matcher.end()
                    + len);
        //替换首次找到的字符串
            str = str.replaceFirst(match, "<comp>" + match
                    + "</comp>");
        //len需要加上s长度
            len = len + s.length();
        }
        return str;
    }
}
