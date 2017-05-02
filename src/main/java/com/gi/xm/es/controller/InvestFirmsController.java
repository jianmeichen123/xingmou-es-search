package com.gi.xm.es.controller;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.pojo.query.InvestFirmsQuery;
import com.gi.xm.es.util.ListUtil;
import com.gi.xm.es.view.MessageStatus;
import com.gi.xm.es.view.Pagination;
import com.gi.xm.es.view.Result;
import io.netty.util.internal.StringUtil;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/search/")
public class InvestFirmsController {

    private static final Logger LOG = LoggerFactory.getLogger(InvestFirmsController.class);

    @Autowired
    private Client client;

    private static final Integer SEARCHLIMIT = 2000;

    private final String INDEX = "ctdn_investfirms";

    private final String TYPE = "investfirms";

    private static Result errorRet = new Result(MessageStatus.MISS_PARAMETER.getMessage(), MessageStatus.MISS_PARAMETER.getStatus());

    @RequestMapping(value="investfirms",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result queryMergeEvent(@RequestBody InvestFirmsQuery investFirmsQuery) {
        Result ret = new Result();
        Integer pageSize = investFirmsQuery.getPageSize();
        Integer pageNum = investFirmsQuery.getPageNo();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //按行业
        if(ListUtil.isNotEmpty(investFirmsQuery.getIndustryIds())){
            queryBuilder.must(QueryBuilders.termsQuery("industryIds",investFirmsQuery.getIndustryIds()));
        }
        //按投资阶段
        if(ListUtil.isNotEmpty(investFirmsQuery.getInvestStageList())){
            queryBuilder.must(QueryBuilders.termsQuery("investStage",investFirmsQuery.getInvestStageList()));
        }
        //按机构类型
        if(ListUtil.isNotEmpty(investFirmsQuery.getOrgTypeList())){
            queryBuilder.must(QueryBuilders.termsQuery("orgType",investFirmsQuery.getOrgTypeList()));
        }
        //按地区
        if(ListUtil.isNotEmpty(investFirmsQuery.getDistrictIds())){
            queryBuilder.must(QueryBuilders.termsQuery("districtId",investFirmsQuery.getDistrictIds()));
        }
        if(ListUtil.isNotEmpty(investFirmsQuery.getDistrictSubIds())){
            queryBuilder.must(QueryBuilders.termsQuery("districtSubId",investFirmsQuery.getDistrictSubIds()));
        }
        //按资本类型
        if(ListUtil.isNotEmpty(investFirmsQuery.getCapitalTypeList())){
            queryBuilder.must(QueryBuilders.termsQuery("capitalType",investFirmsQuery.getCapitalTypeList()));
        }
        if(ListUtil.isNotEmpty(investFirmsQuery.getCurrencyList())){
            queryBuilder.must(QueryBuilders.termsQuery("currentcyTitle",investFirmsQuery.getCurrencyList()));
        }
        if(StringUtils.isEmpty(investFirmsQuery.getOrgName())){
            queryBuilder.must(QueryBuilders.wildcardQuery("orgName","*"+investFirmsQuery.getOrgName()+"*"));
        }

        //设置分页参数和请求参数
        SearchRequestBuilder sb = client.prepareSearch(INDEX);
        sb.setQuery(queryBuilder);
        //求总数
        SearchResponse res =sb.setTypes(TYPE).setSearchType(SearchType.DEFAULT).execute().actionGet();
        Long  totalHit = res.getHits().totalHits();

        if(StringUtils.isEmpty(investFirmsQuery.getOrder())){
            sb.addSort(investFirmsQuery.getOrderBy(), SortOrder.fromString(investFirmsQuery.getOrderBy()));
        }else {
            sb.addSort("newestInvestDate", SortOrder.DESC);
        }
        sb.setFrom(pageNum);
        sb.setSize(pageSize);
        //返回响应
        SearchResponse response =sb.setTypes(TYPE).setSearchType(SearchType.DEFAULT).execute().actionGet();
        SearchHits shs = response.getHits();
        List<Object> entityList = new ArrayList<>();
        for (SearchHit it : shs) {
            Map source = it.getSource();
            InvestFirmsQuery entity =  JSON.parseObject(JSON.toJSONString(source),InvestFirmsQuery.class);
            //获取对应的高亮域
            Map<String, HighlightField> result = it.highlightFields();
            //从设定的高亮域中取得指定域
            for (Map.Entry<String, HighlightField> entry : result.entrySet()) {
                String key = entry.getKey();
                try {
                    //获得高亮字段的原值
                    Field field = entity.getClass().getDeclaredField(key);
                    field.setAccessible(true);
                    String value = field.get(entity).toString();
                    //获得搜索关键字
                    String rep = "<firm>"+entity.getOrgName()+"</firm>";
                    //替换
                    field.set(entity, value.replaceAll(entity.getOrgName(),rep));
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                    return errorRet;
                }
            }
            entityList.add(entity);
        }
        Pagination page = new Pagination();
        page.setTotal(totalHit>SEARCHLIMIT?SEARCHLIMIT:totalHit);
        page.setTotalhit(totalHit);
        page.setRecords(entityList);
        ret = new Result(MessageStatus.OK.getMessage(), MessageStatus.OK.getStatus(), page);
        return ret;
    }
}