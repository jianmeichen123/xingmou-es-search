package com.gi.xm.es.service;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.pojo.Query;
import com.gi.xm.es.pojo.query.InvestFirmsQuery;
import com.gi.xm.es.pojo.query.ProjectQuery;
import com.gi.xm.es.util.ListUtil;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
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

/**
 * Created by zcy on 17-11-4.
 */
@Service
public class InvestfirmsService extends BaseService {

    @Autowired
    private Client client;

    @Value("${ctdn.investfirms.index}")
    private  String index;

    @Value("${ctdn.investfirms.type}")
    private  String type;

    /**构建请求体:
     *全文搜索查符合条件项目数
     * @return
     */
    public  Long queryNum(Query query){
        InvestFirmsQuery investFirmsQuery = new InvestFirmsQuery();
        investFirmsQuery.setKeyword(query.getKeyword());
        SearchRequestBuilder qb = queryList(investFirmsQuery);
        SearchHits ssh = getSearchHits(qb);
        return ssh.getTotalHits();
    }

    /**
     * 按多条件查询投资事件列表
     * @return
     */
    public SearchRequestBuilder queryList(InvestFirmsQuery investFirmsQuery){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        SearchRequestBuilder srb = client.prepareSearch(index);
        //按行业
        if(ListUtil.isNotEmpty(investFirmsQuery.getIndustryIds())){
            queryBuilder.must(QueryBuilders.termsQuery("industryIds",investFirmsQuery.getIndustryIds()));
        }
        //按投资阶段
//        if(ListUtil.isNotEmpty(investFirmsQuery.getInvestStages())){
//            queryBuilder.must(QueryBuilders.termsQuery("investStage",investFirmsQuery.getInvestStages()));
//        }
        //按机构类型
//        if(ListUtil.isNotEmpty(investFirmsQuery.getOrgTypes())){
//            queryBuilder.must(QueryBuilders.termsQuery("orgType",investFirmsQuery.getOrgTypes()));
//        }
        //按轮次
        if(ListUtil.isNotEmpty(investFirmsQuery.getOrgRounds())){
            queryBuilder.must(QueryBuilders.termsQuery("investRounds",investFirmsQuery.getOrgRounds()));
        }
        //按地区
        if (ListUtil.isNotEmpty(investFirmsQuery.getDistrictIds())&&ListUtil.isNotEmpty(investFirmsQuery.getDistrictSubIds())) {
            BoolQueryBuilder shoudBuilder = QueryBuilders.boolQuery();
            shoudBuilder.should(QueryBuilders.termsQuery("districtId", investFirmsQuery.getDistrictIds()));
            shoudBuilder.should(QueryBuilders.termsQuery("districtSubId", investFirmsQuery.getDistrictSubIds()));
            shoudBuilder.minimumNumberShouldMatch(1);
            queryBuilder.must(shoudBuilder);
        }else if (ListUtil.isNotEmpty(investFirmsQuery.getDistrictIds())) {
            queryBuilder.must(QueryBuilders.termsQuery("districtId", investFirmsQuery.getDistrictIds()));
        }else if (ListUtil.isNotEmpty(investFirmsQuery.getDistrictSubIds())) {
            queryBuilder.must(QueryBuilders.termsQuery("districtSubId", investFirmsQuery.getDistrictSubIds()));
        }
        //按资本类型
//        if(ListUtil.isNotEmpty(investFirmsQuery.getCapitalTypes())){
//            queryBuilder.must(QueryBuilders.termsQuery("capitalType",investFirmsQuery.getCapitalTypes()));
//        }
//        if(ListUtil.isNotEmpty(investFirmsQuery.getCurrencys())){
//            queryBuilder.must(QueryBuilders.termsQuery("currencyType",investFirmsQuery.getCurrencys()));
//        }
        if(!StringUtils.isEmpty(investFirmsQuery.getKeyword())){
            investFirmsQuery.setInvestOrg(QueryParserBase.escape((investFirmsQuery.getKeyword().trim())));
            queryBuilder.must(QueryBuilders.wildcardQuery("investOrg","*"+investFirmsQuery.getKeyword()+"*"));
            //设置高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("investOrg");
            srb.highlighter(highlightBuilder);
        }

        //设置分页参数和请求参数
        srb.setQuery(queryBuilder);
        //求总数
        SearchResponse res =srb.setTypes(type).setSearchType(SearchType.DEFAULT).execute().actionGet();
        Long  totalHit = res.getHits().totalHits();

        if(!StringUtils.isEmpty(investFirmsQuery.getOrderBy())){
            srb.addSort(investFirmsQuery.getOrderBy(), SortOrder.fromString(investFirmsQuery.getOrder()));
        }else {
            srb.addSort("investTotal", SortOrder.DESC);
        }
        //设置分页参数和请求参数
        Integer tmp = investFirmsQuery.getPageSize();
        Integer pageSize = investFirmsQuery.getPageSize();
        Integer pageNo = investFirmsQuery.getPageNo();
        if (pageSize*pageNo+pageSize > max_search_result){
            tmp =  max_search_result - pageSize*pageNo;
        }
        srb.setFrom(pageNo*pageSize).setSize(tmp);
        return srb;
    }

    /**
     * 查询结果转List
     * @param investFirmsQuery
     * @param shs
     * @return
     * @throws Exception
     */
    public  List<Object> getResponseList (InvestFirmsQuery investFirmsQuery,SearchHits shs)  throws Exception {
        List<Object> entityList = new ArrayList<>();
        for (SearchHit it : shs) {
            Map source = it.getSource();
            InvestFirmsQuery entity =  JSON.parseObject(JSON.toJSONString(source),InvestFirmsQuery.class);
            //获取对应的高亮域
            Map<String, HighlightField> result = it.highlightFields();
            //从设定的高亮域中取得指定域
            if(result != null){
                for (Map.Entry<String, HighlightField> entry : result.entrySet()) {
                    String key = entry.getKey();
                    try {
                        //获得高亮字段的原值
                        Field field = entity.getClass().getDeclaredField(key);
                        field.setAccessible(true);
                        String value = field.get(entity).toString();
                        //获得搜索关键字
                        String rep = "<firm>"+investFirmsQuery.getInvestOrg()+"</firm>";
                        //替换
                        field.set(entity, value.replaceAll(investFirmsQuery.getInvestOrg(),rep));
                    } catch (Exception e) {
                        throw e;
                    }
                }
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
