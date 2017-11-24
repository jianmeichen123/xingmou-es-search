package com.gi.xm.es.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gi.xm.es.pojo.Query;
import com.gi.xm.es.pojo.query.LaunchEventQuery;
import com.gi.xm.es.pojo.query.MergeEventQuery;
import com.gi.xm.es.util.ListUtil;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.join.ScoreMode;
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
public class MergeEventService extends BaseService {


    @Autowired
    private Client client;


    @Value("${ctdn.merge_event.index}")
    private  String index;

    @Value("${ctdn.merge_event.type}")
    private  String type;

    /**构建请求体:
     *全文搜索查符合条件并购事件数
     * @return
     */
    public  Long queryNum(Query query){
        MergeEventQuery mergeEventQuery = new MergeEventQuery();
        mergeEventQuery.setKeyword(query.getKeyword());
        SearchRequestBuilder qb = queryList(mergeEventQuery);
        SearchHits ssh = getSearchHits(qb);
        return ssh.getTotalHits();
    }

    /**
     * 按多条件查询投资事件列表
     * @return
     */
    public SearchRequestBuilder queryList(MergeEventQuery mergeEventQuery){
        SearchRequestBuilder srb = client.prepareSearch(index);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //按行业
        if (ListUtil.isNotEmpty(mergeEventQuery.getIndustryIds())) {
            queryBuilder.must(QueryBuilders.termsQuery("industryIds", mergeEventQuery.getIndustryIds()));
        }
        //按title
        if (!StringUtils.isEmpty(mergeEventQuery.getKeyword())) {
            mergeEventQuery.setKeyword(QueryParserBase.escape(mergeEventQuery.getKeyword().trim()));
            BoolQueryBuilder shoudBuilder = QueryBuilders.boolQuery();
            shoudBuilder.should(QueryBuilders.wildcardQuery("projTitle", "*" +  mergeEventQuery.getKeyword() + "*"));
            shoudBuilder.should(QueryBuilders.nestedQuery("mergeSideJson",QueryBuilders.wildcardQuery("mergeSideJson.title","*"+mergeEventQuery.getKeyword()+"*"), ScoreMode.None));
            shoudBuilder.minimumNumberShouldMatch(1);
            queryBuilder.must(shoudBuilder);
        }
        //股权占比
        if (ListUtil.isNotEmpty(mergeEventQuery.getEquityRates())) {
            queryBuilder.must(QueryBuilders.termsQuery("equityrateRange", mergeEventQuery.getEquityRates()));
        }
        //按币种
        if (ListUtil.isNotEmpty(mergeEventQuery.getCurrencyTypes())) {
            queryBuilder.must(QueryBuilders.termsQuery("currencyType", mergeEventQuery.getCurrencyTypes()));
        }
        //按并购状态
//        if (ListUtil.isNotEmpty(mergeEvent.getMergeStates())) {
//            queryBuilder.must(QueryBuilders.termsQuery("mergeState", mergeEvent.getMergeStates()));
//        }
        //按并购类型
//        if (ListUtil.isNotEmpty(mergeEvent.getMergeTypes())) {
//            queryBuilder.must(QueryBuilders.termsQuery("mergeType", mergeEvent.getMergeTypes()));
//        }
        //按并购结束时间
        if (!StringUtils.isEmpty(mergeEventQuery.getStartDate()) || !StringUtils.isEmpty(mergeEventQuery.getEndDate())) {
            RangeQueryBuilder rangeq = QueryBuilders.rangeQuery("mergeDate");
            if (!StringUtils.isEmpty(mergeEventQuery.getStartDate())) {
                rangeq.gte(mergeEventQuery.getStartDate());
            }
            if (!StringUtils.isEmpty(mergeEventQuery.getEndDate())) {
                rangeq.lte(mergeEventQuery.getEndDate());
            }
            queryBuilder.filter(rangeq);
        }

        if (ListUtil.isNotEmpty(mergeEventQuery.getCurrencyTypes())) {
            queryBuilder.must(QueryBuilders.termsQuery("currencyType", mergeEventQuery.getCurrencyTypes()));
        }
        //设置分页参数和请求参数
        srb.setQuery(queryBuilder);
        //求总数
        SearchResponse res = srb.setTypes(type).setSearchType(SearchType.DEFAULT).execute().actionGet();
        Long totalHit = res.getHits().totalHits();

        if (!StringUtils.isEmpty(mergeEventQuery.getOrderBy())) {
            srb.addSort(mergeEventQuery.getOrderBy(), SortOrder.fromString(mergeEventQuery.getOrder()));
        } else {
            srb.addSort("mergeDate", SortOrder.DESC);
        }
        //设置分页参数和请求参数
        Integer tmp = mergeEventQuery.getPageSize();
        Integer pageSize = mergeEventQuery.getPageSize();
        Integer pageNo = mergeEventQuery.getPageNo();
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
    public  List<Object> getResponseList (MergeEventQuery mergeEventQuery,SearchHits shs)  throws Exception {
        List<Object> entityList = new ArrayList<>();
        for (SearchHit it : shs) {
            try {
                Map source = it.getSource();
                MergeEventQuery entity = JSON.parseObject(JSON.toJSONString(source), MergeEventQuery.class);
                //获取对应的高亮域
                Map<String, HighlightField> result = it.highlightFields();
                //从设定的高亮域中取得指定域
                //高亮company
                if (!StringUtils.isEmpty(mergeEventQuery.getKeyword())) {
                    Field field1 = entity.getClass().getDeclaredField("projTitle");
                    field1.setAccessible(true);
                    Object object1 = field1.get(entity);
                    //判断是否有该属性
                    if (object1 != null) {
                        String value1 = object1.toString();
                        if (value1 != null) {
                            field1.set(entity, value1.replaceAll(mergeEventQuery.getKeyword(), "<comp>" + mergeEventQuery.getKeyword() + "</comp>"));
                        }
                    }
                }
                //重新构造investSideJson,使之成为json,便于解析
                Field field2 = entity.getClass().getDeclaredField("mergeSideJson");
                field2.setAccessible(true);
                Object object = field2.get(entity);
                //判断是否有该属性
                if (object != null) {
                    JSONArray ls = JSONArray.parseArray(object.toString());
                    if(ls!=null && ls.size()>0){
                        JSONArray resultJsonArray = new JSONArray(3);
                        if (!StringUtils.isEmpty(mergeEventQuery.getKeyword())) {
                            for (int i = 0; i < ls.size(); i++) {
                                JSONObject json = (JSONObject) ls.get(i);
                                String invstor = (String) json.get("invstor");
                                if (invstor.indexOf(mergeEventQuery.getKeyword()) >= 0) {
                                    if (resultJsonArray.size() <= 3) {
                                        json.put("invstor", invstor.replaceAll(mergeEventQuery.getKeyword(), "<firm>" + mergeEventQuery.getKeyword() + "</firm>"));
                                        resultJsonArray.add(json);
                                    }
                                }
                            }
                        }
                        for (int i= 0;i<ls.size();i++) {
                            JSONObject json = (JSONObject)ls.get(i);
                            if(resultJsonArray.size()<3){
                                resultJsonArray.add(json);
                            }
                        }
                        field2.set(entity,resultJsonArray.toString());
                    }
                }
                entityList.add(entity);
            } catch (Exception e) {
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
