package com.gi.xm.es.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gi.xm.es.controller.NewsController;
import com.gi.xm.es.pojo.Query;
import com.gi.xm.es.pojo.query.NewsQuery;
import com.gi.xm.es.view.MessageStatus;
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
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.tophits.InternalTopHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zcy on 17-11-4.
 */
@Service
public class NewsService extends BaseService {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(NewsService.class);

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
        if(!StringUtils.isEmpty(newsQuery.getIndustryNames())){
        	
        	queryBuilder.must(QueryBuilders.termsQuery("industry",  newsQuery.getIndustryNames().split(",")));
        }
        //按code
//        if(!StringUtils.isEmpty(newsQuery.getCode())){
//            queryBuilder.must(QueryBuilders.termQuery("code",newsQuery.getCode()));
//        }

        //设置分页参数和请求参数
        srb.setQuery(queryBuilder);
        srb.addSort("orderTime", SortOrder.DESC);
        srb.addSort("_score", SortOrder.DESC);
        System.out.println(srb.toString());
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
                    title.set(p,ignoreCase(titleVal,newsQuery.getKeyword()));
                    Field overview = p.getClass().getDeclaredField("overview");
                    overview.setAccessible(true);
                    String overviewVal = overview.get(p).toString();
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

    private static String ignoreCase(String str,String regex) {
        //保存你要添加的html代码的长度
        int len = 0;
        String s = "<comp></comp>";
        //不区分大小写匹配字符串
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        //循环查找，可能匹配到的不止一个字符串
        while (matcher.find()) {
            String match = str.substring(matcher.start() + len, matcher.end() + len);
            if(regex.equals(match)){
                str = str.replaceAll(match, "<comp>" + match + "</comp>");
                break;
            }
            //替换首次找到的字符串
            str = str.replace(match, "<comp>" + match + "</comp>");
            //len需要加上s长度
            len = len + s.length();
        }
        return str;
    }
    
    public List<SearchHits >  queryGGList(NewsQuery newsQuery) throws Exception {
		 SearchRequestBuilder srb = client.prepareSearch("ctdn_news");
		
	     srb.addSort("orderTime", SortOrder.DESC);
	     srb.setFrom(0).setSize(2);
	  
		 Calendar cal = Calendar.getInstance();
		 int interval = 0;
		 List<SearchHits > result = new ArrayList<SearchHits >();
		 while(true){
			 BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		     if(newsQuery.getIndustryNames()!=null && newsQuery.getIndustryNames().trim().length()!=0){
		    	 queryBuilder.must(QueryBuilders.termsQuery("industry",  newsQuery.getIndustryNames().split(",")));
		     }
		     queryBuilder.must(QueryBuilders.termQuery("typeId", newsQuery.getTypeId()));
			 cal.setTimeInMillis(System.currentTimeMillis());
			 cal.add(Calendar.DATE, -interval);
			 interval++;
			 String now_format =  new SimpleDateFormat("yyyy-MM-dd").format(new Date(cal.getTimeInMillis()));
			 long end_time =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(now_format + " 23:59:59").getTime()/1000;
			 long start_time =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(now_format + " 00:00:00").getTime()/1000;
			 LOG.info(interval + "当前日期：" + now_format + "start_time =" +  start_time + " end_time = " + end_time);
			 queryBuilder.must(QueryBuilders.rangeQuery("orderTime").gte(start_time).lt(end_time));
			 System.out.println(queryBuilder.toString());
		     srb.setQuery(queryBuilder);
	        SearchResponse res = srb.setTypes("news").setSearchType(SearchType.DEFAULT).execute().actionGet();
	        SearchHits shs = res.getHits();
	        System.out.println(shs.getTotalHits());
	        if(shs.getTotalHits() == 0){
	        	 if(interval == 20){//20天以前停止查询
	 	        	break;
	 	        }
	        	continue;
	        }
	        result.add(res.getHits());
	        if(result.size() ==3){
	        	break;
	        }
		 }
		 return result;
	}
	
	public SearchHits  getGGCompeteInfo(NewsQuery newsQuery) throws Exception {
		 SearchRequestBuilder srb = client.prepareSearch("ctdn_news");
	     srb.addSort("orderTime", SortOrder.DESC);
	     srb.setFrom(0).setSize(3);
	     BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		 queryBuilder.must(QueryBuilders.matchPhraseQuery("title",  newsQuery.getIndustryNames()));
		 queryBuilder.must(QueryBuilders.termQuery("typeId", "1"));
		 System.out.println(queryBuilder.toString());
		 srb.setQuery(queryBuilder);
	     SearchResponse res = srb.setTypes("news").setSearchType(SearchType.DEFAULT).execute().actionGet();
		 return res.getHits();
	}

    //按类型分组,返回每组总数和前n条数据
    public  JSONObject getAggregationResponse(NewsQuery newsQuery,SearchRequestBuilder srb){
        JSONObject json = new JSONObject();
        json.put("status", MessageStatus.OK.getStatus());
        json.put("message",MessageStatus.OK.getMessage());
        SearchResponse searchResponse = srb.get();
        Map<String, Aggregation> aggMap = searchResponse.getAggregations().asMap();
        LongTerms gradeTerms = (LongTerms) aggMap.get("perType");
        Iterator<Terms.Bucket> gradeBucketIt = gradeTerms.getBuckets().iterator();
        try{
            JSONArray array = new JSONArray();
            while(gradeBucketIt.hasNext()){
                JSONObject obj = new JSONObject();
                Terms.Bucket gradeBucket = gradeBucketIt.next();
                Long key = (Long)gradeBucket.getKey();
                Long count = gradeBucket.getDocCount();
                obj.put("typeId",key);
                obj.put("number",count);
                InternalTopHits topHits =(InternalTopHits)gradeBucket.getAggregations().asMap().get("topHit");
                SearchHits searchHits = topHits.getHits();
                List<Object> entityList = getResponseList(newsQuery,searchHits);
                obj.put("newsList",entityList);
                array.add(obj);
            }
            json.put("data",array);
        }catch (Exception e){
            json.put("status", MessageStatus.SYS_ERROR.getStatus());
            json.put("message",MessageStatus.SYS_ERROR.getMessage());
            json.put("data",null);
            e.printStackTrace();
        }
        return json;
    }

    public static void main(String[] args) {
        String str = "美丽说JAVA,美丽说JAVA优惠券";
        String regex = "JAVA";
        //保存你要添加的html代码的长度
        int len = 0;
        String s = "<comp></comp>";
//不区分大小写匹配字符串
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
    //循环查找，可能匹配到的不止一个字符串

        while(matcher.find()) {

//截取字符串，临时保存匹配到的字符串
//起始位置和结束位置都要加一个len长度
            String match = str.substring(matcher.start() + len, matcher.end()
                    + len);
            if(regex.equals(match)){
                str = str.replaceAll(match, "<comp>" + match + "</comp>");
                break;
            }
//替换首次找到的字符串
            str = str.replaceFirst(match, "<comp>" + match
                    + "</comp>");
//len需要加上s长度

            len = len + s.length();
        }
        System.out.println(str);
    }
}
