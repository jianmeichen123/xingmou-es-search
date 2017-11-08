package com.gi.xm.es.service;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.pojo.Query;
import com.gi.xm.es.pojo.query.InvestFirmsQuery;
import com.gi.xm.es.pojo.query.LaunchEventQuery;
import com.gi.xm.es.util.ListUtil;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
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

/**
 * Created by zcy on 17-11-4.
 */
@Service
public class LaunchEventService extends BaseService {


    @Autowired
    private Client client;

    @Value("${ctdn.launch_event.index}")
    private  String index;

    @Value("${ctdn.launch_event.type}")
    private  String type;

    /**构建请求体:
     *全文搜索查符合条件上市挂牌数
     * @return
     */
    public  Long queryNum(Query query){
        LaunchEventQuery launchEventQuery = new LaunchEventQuery();
        launchEventQuery.setKeyword(query.getKeyword());
        SearchRequestBuilder qb = queryList(launchEventQuery);
        SearchHits ssh = getSearchHits(qb);
        return ssh.getTotalHits();
    }

    /**
     * 按多条件查询投资事件列表
     * @return
     */
    public SearchRequestBuilder queryList(LaunchEventQuery launchEventQuery){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        SearchRequestBuilder srb = client.prepareSearch(index);
        //按行业
        if(ListUtil.isNotEmpty(launchEventQuery.getIndustryIds())){
            queryBuilder.must(QueryBuilders.termsQuery("industryIds",launchEventQuery.getIndustryIds()));
        }
        //按title
        if(!StringUtils.isEmpty(launchEventQuery.getKeyword())){
            launchEventQuery.setProjTitle(QueryParserBase.escape(launchEventQuery.getKeyword().trim()));
            queryBuilder.must(QueryBuilders.wildcardQuery("projTitle", "*" + launchEventQuery.getKeyword() + "*"));
            //设置高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("projTitle");
            srb.highlighter(highlightBuilder);
        }
        if(ListUtil.isNotEmpty(launchEventQuery.getTypes())){
            queryBuilder.must(QueryBuilders.termsQuery("type",launchEventQuery.getTypes()));
        }
//        //按type
//        if(!StringUtils.isEmpty(launchEventQuery.getType())){
//            queryBuilder.must(QueryBuilders.termQuery("type",launchEventQuery.getType()));
//        }
//        //按交易所
//        if(ListUtil.isNotEmpty(launchEventQuery.getStockExchanges())){
//            queryBuilder.must(QueryBuilders.termsQuery("stockExchange",launchEventQuery.getStockExchanges()));
//        }
//        //按转让方式
//        if(ListUtil.isNotEmpty(launchEventQuery.getTransferTypes())){
//            queryBuilder.must(QueryBuilders.termsQuery("transferType",launchEventQuery.getTransferTypes()));
//        }
//        //市场封层
//        if(ListUtil.isNotEmpty(launchEventQuery.getMarketLayers())){
//            queryBuilder.must(QueryBuilders.termsQuery("marketLayer",launchEventQuery.getMarketLayers()));
//        }
        //按createDate
        if(!StringUtils.isEmpty(launchEventQuery.getStartDate()) || !StringUtils.isEmpty(launchEventQuery.getEndDate())){
            RangeQueryBuilder rangeq = QueryBuilders.rangeQuery("listedDate");
            if(!StringUtils.isEmpty(launchEventQuery.getStartDate())){
                rangeq.gte(launchEventQuery.getStartDate());
            }
            if(!StringUtils.isEmpty(launchEventQuery.getEndDate())){
                rangeq.lte(launchEventQuery.getEndDate());
            }
            queryBuilder.filter(rangeq);
        }
        //设置分页参数和请求参数
        srb.setQuery(queryBuilder);
        if(!StringUtils.isEmpty(launchEventQuery.getOrderBy())){
            srb.addSort(launchEventQuery.getOrderBy(), SortOrder.fromString(launchEventQuery.getOrder()));
        }else {
            srb.addSort("listedDate", SortOrder.DESC);
        }
        //求总数
        SearchResponse res =srb.setTypes(type).setSearchType(SearchType.DEFAULT).execute().actionGet();
        Long  totalHit = res.getHits().totalHits();
        //设置分页参数和请求参数
        Integer tmp = launchEventQuery.getPageSize();
        Integer pageSize = launchEventQuery.getPageSize();
        Integer pageNo = launchEventQuery.getPageNo();
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
    public  List<Object> getResponseList (LaunchEventQuery launchEventQuery,SearchHits shs)  throws Exception {
        List<Object> entityList = new ArrayList<>();
        for (SearchHit it : shs) {
            Map source = it.getSource();
            LaunchEventQuery entity = JSON.parseObject(JSON.toJSONString(source), LaunchEventQuery.class);
            //获取对应的高亮域
            Map<String, HighlightField> result = it.highlightFields();
            //从设定的高亮域中取得指定域
            try {
                for (Map.Entry<String, HighlightField> entry : result.entrySet()) {
                    String key = entry.getKey();

                    //获得高亮字段的原值
                    Field field = entity.getClass().getDeclaredField(key);
                    field.setAccessible(true);
                    String value = field.get(entity).toString();
                    //获得搜索关键字
                    String rep = "<comp>" + launchEventQuery.getProjTitle() + "</comp>";
                    //替换
                    field.set(entity, value.replaceAll(launchEventQuery.getProjTitle(), rep));
                }
            } catch (Exception e) {
                throw e;
            }
            entityList.add(entity);
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
