package com.gi.xm.es.controller;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.pojo.Pagination;
import com.gi.xm.es.pojo.query.InvestEventQuery;
import com.gi.xm.es.pojo.query.LaunchEventQuery;
import com.gi.xm.es.util.ListUtil;
import com.gi.xm.es.view.MessageStatus;
import com.gi.xm.es.view.Result;
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
public class LaunchEventController {

    private static final Logger LOG = LoggerFactory.getLogger(LaunchEventController.class);

    @Autowired
    private Client client;

    private static final Integer SEARCHLIMIT = 2000;

    private final String INDEX = "ctdn_launch_event";

    private final String TYPE = "launch_event";

    private static Result errorRet = new Result(MessageStatus.MISS_PARAMETER.getMessage(), MessageStatus.MISS_PARAMETER.getStatus());

    @RequestMapping(value="launchEvent",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result queryInvestEvent(@RequestBody LaunchEventQuery launchEventQuery) {
        Result ret = new Result();
        Integer pageSize = launchEventQuery.getPageSize();
        Integer pageNum = launchEventQuery.getPageNo();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        SearchRequestBuilder sb = client.prepareSearch(INDEX);
        //按行业
        if(ListUtil.isNotEmpty(launchEventQuery.getIndustryIds())){
            queryBuilder.must(QueryBuilders.termsQuery("industryIds",launchEventQuery.getIndustryIds()));
        }
        //按title
        if(!StringUtils.isEmpty(launchEventQuery.getProjTitle())){
            queryBuilder.should(QueryBuilders.wildcardQuery("projTitle","*"+launchEventQuery.getProjTitle()+"*"));
            //设置高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("projTitle");
            sb.highlighter(highlightBuilder);
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
        sb.setQuery(queryBuilder);
        if(!StringUtils.isEmpty(launchEventQuery.getOrderBy())){
            sb.addSort(launchEventQuery.getOrderBy(), SortOrder.fromString(launchEventQuery.getOrder()));
        }else {
            sb.addSort("listedDate", SortOrder.DESC);
        }
        //求总数
        SearchResponse res =sb.setTypes(TYPE).setSearchType(SearchType.DEFAULT).execute().actionGet();
        Long  totalHit = res.getHits().totalHits();
        Integer tmp = pageSize;
        if (pageSize*pageNum+pageSize > SEARCHLIMIT){
            tmp =  SEARCHLIMIT - pageSize*pageNum;
        }
        sb.setFrom(pageNum*pageSize).setSize(tmp);
        //返回响应
        SearchResponse response =sb.setTypes(TYPE).setSearchType(SearchType.DEFAULT).execute().actionGet();
        SearchHits shs = response.getHits();
        List<Object> entityList = new ArrayList<>();
        for (SearchHit it : shs) {
            Map source = it.getSource();
            LaunchEventQuery entity =  JSON.parseObject(JSON.toJSONString(source),LaunchEventQuery.class);
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
                    String rep = "<comp>"+launchEventQuery.getProjTitle()+"</comp>";
                    //替换
                    field.set(entity, value.replaceAll(launchEventQuery.getProjTitle(),rep));
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