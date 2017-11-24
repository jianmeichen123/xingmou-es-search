package com.gi.xm.es.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gi.xm.es.pojo.Query;
import com.gi.xm.es.pojo.query.InvestEventQuery;
import com.gi.xm.es.pojo.query.ProjectQuery;
import com.gi.xm.es.util.ListUtil;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
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
public class InvestEventService extends BaseService {

    @Autowired
    private Client client;

    @Value("${ctdn.invest_event.index}")
    private  String index;

    @Value("${ctdn.invest_event.type}")
    private  String type;


    /**构建请求体:
     *全文搜索查符合条件投资事件list
     * @param query keyword 查询关键字
     * @param index 索引名称
     * @return
     */
    /**构建请求体:
     *全文搜索查符合条件项目数
     * @return
     */
    public  Long queryNum(Query query){
        InvestEventQuery investEvent = new InvestEventQuery();
        investEvent.setKeyword(query.getKeyword());
        SearchRequestBuilder qb = queryList(investEvent);
        SearchHits ssh = getSearchHits(qb);
        return ssh.getTotalHits();
    }

    /**
     * 按多条件查询投资事件列表
     * @return
     */
    public SearchRequestBuilder queryList(InvestEventQuery investEvent){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        SearchRequestBuilder srb = client.prepareSearch(index);
        //按行业
        if(ListUtil.isNotEmpty(investEvent.getIndustryIds())){
            queryBuilder.must(QueryBuilders.termsQuery("industryIds",investEvent.getIndustryIds()));
        }
        //按title
        if(!StringUtils.isEmpty(investEvent.getKeyword())){
            investEvent.setCompany(QueryParserBase.escape(investEvent.getKeyword().trim()));
            BoolQueryBuilder shoudBuilder = QueryBuilders.boolQuery();
            shoudBuilder.should(QueryBuilders.wildcardQuery("company","*"+ investEvent.getKeyword()+"*"));
            shoudBuilder.should(QueryBuilders.nestedQuery("investSideJson",QueryBuilders.wildcardQuery("investSideJson.invstor","*"+investEvent.getKeyword()+"*"), ScoreMode.None));
            shoudBuilder.minimumNumberShouldMatch(1);
            queryBuilder.must(shoudBuilder);
        }
        //按round
        if(ListUtil.isNotEmpty(investEvent.getInvestRounds())){
            queryBuilder.must(QueryBuilders.termsQuery("round",investEvent.getInvestRounds()));
        }
        //按investdate
        if(!StringUtils.isEmpty(investEvent.getStartDate()) || !StringUtils.isEmpty(investEvent.getEndDate())){
            RangeQueryBuilder rangeq = QueryBuilders.rangeQuery("investDate");
            if(!StringUtils.isEmpty(investEvent.getStartDate())){
                rangeq.gte(investEvent.getStartDate());
            }
            if(!StringUtils.isEmpty(investEvent.getEndDate())){
                rangeq.lte(investEvent.getEndDate());
            }
            queryBuilder.filter(rangeq);
        }

        //按地区
        if (ListUtil.isNotEmpty(investEvent.getDistrictIds())&&ListUtil.isNotEmpty(investEvent.getDistrictSubIds())) {
            BoolQueryBuilder shoudBuilder = QueryBuilders.boolQuery();
            shoudBuilder.should(QueryBuilders.termsQuery("districtId", investEvent.getDistrictIds()));
            shoudBuilder.should(QueryBuilders.termsQuery("districtSubId", investEvent.getDistrictSubIds()));
            shoudBuilder.minimumNumberShouldMatch(1);
            queryBuilder.must(shoudBuilder);
        }else if (ListUtil.isNotEmpty(investEvent.getDistrictIds())) {
            queryBuilder.must(QueryBuilders.termsQuery("districtId", investEvent.getDistrictIds()));
        }else if (ListUtil.isNotEmpty(investEvent.getDistrictSubIds())) {
            queryBuilder.must(QueryBuilders.termsQuery("districtSubId", investEvent.getDistrictSubIds()));
        }

        if(ListUtil.isNotEmpty(investEvent.getCurrencyTypes())){
            queryBuilder.must(QueryBuilders.termsQuery("currencyType",investEvent.getCurrencyTypes()));
        }
        //设置分页参数和请求参数
        srb.setQuery(queryBuilder);
        if(!StringUtils.isEmpty(investEvent.getOrderBy())){
            srb.addSort("investDate", SortOrder.fromString(investEvent.getOrder()));
        }else{
            srb.addSort("investDate", SortOrder.DESC);
        }
        //设置分页参数和请求参数
        Integer tmp = investEvent.getPageSize();
        Integer pageSize = investEvent.getPageSize();
        Integer pageNo = investEvent.getPageNo();
        if (pageSize*pageNo+pageSize > max_search_result){
            tmp =  max_search_result - pageSize*pageNo;
        }
        srb.setFrom(pageNo*pageSize).setSize(tmp);
        return srb;
    }

    /**
     * 查询结果转List
     * @param investEvent
     * @param shs
     * @return
     * @throws Exception
     */
    public  List<Object> getResponseList (InvestEventQuery investEvent,SearchHits shs)  throws Exception{
        List<Object> entityList = new ArrayList<>();
        for (SearchHit it : shs) {
            try {
                Map source = it.getSource();
                InvestEventQuery entity = JSON.parseObject(JSON.toJSONString(source), InvestEventQuery.class);
                //高亮company
                if (!StringUtils.isEmpty(investEvent.getCompany())) {
                    Field field1 = entity.getClass().getDeclaredField("company");
                    field1.setAccessible(true);
                    Object object1 = field1.get(entity);
                    //判断是否有该属性
                    if (object1 != null) {
                        String value1 = object1.toString();
                        if (value1 != null) {
                            field1.set(entity, value1.replaceAll(investEvent.getCompany(), "<comp>" + investEvent.getCompany() + "</comp>"));
                        }
                    }
                }
                //investSideJson解析,有高亮的放前面
                Field field2 = entity.getClass().getDeclaredField("investSideJson");
                field2.setAccessible(true);
                Object object = field2.get(entity);
                //判断是否有该属性
                if (object != null) {
                    JSONArray ls = JSONArray.parseArray(object.toString());
                    if (ls != null && ls.size() > 0) {
                        JSONArray resultJsonArray = new JSONArray(3);
                        if (!StringUtils.isEmpty(investEvent.getCompany())) {
                            for (int i = 0; i < ls.size(); i++) {
                                JSONObject json = (JSONObject) ls.get(i);
                                String invstor = (String) json.get("invstor");
                                if (invstor.indexOf(investEvent.getCompany()) >= 0) {
                                    if (resultJsonArray.size() <= 3) {
                                        json.put("invstor", invstor.replaceAll(investEvent.getCompany(), "<firm>" + investEvent.getCompany() + "</firm>"));
                                        resultJsonArray.add(json);
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < ls.size(); i++) {
                            JSONObject json = (JSONObject) ls.get(i);
                            if (resultJsonArray.size() < 3) {
                                resultJsonArray.add(json);
                            }
                        }

                        field2.set(entity, resultJsonArray.toString());
                    }
                }
                entityList.add(entity);
            }catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
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
