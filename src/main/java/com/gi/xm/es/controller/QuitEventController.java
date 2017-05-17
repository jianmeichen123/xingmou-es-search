package com.gi.xm.es.controller;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.pojo.query.QuitEventQuery;
import com.gi.xm.es.util.ListUtil;
import com.gi.xm.es.view.MessageStatus;
import com.gi.xm.es.view.Pagination;
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
public class QuitEventController {

    private static final Logger LOG = LoggerFactory.getLogger(QuitEventController.class);

    @Autowired
    private Client client;

    private static final Integer SEARCHLIMIT = 2000;

    private final String INDEX = "ctdn_quit_event";

    private final String TYPE = "quit_event";

    private static Result errorRet = new Result(MessageStatus.MISS_PARAMETER.getMessage(), MessageStatus.MISS_PARAMETER.getStatus());

    @RequestMapping(value="quitEvent",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result queryMergeEvent(@RequestBody QuitEventQuery quitEvent) {
        Result ret = new Result();
        Integer pageSize = quitEvent.getPageSize();
        Integer pageNum = quitEvent.getPageNo();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //按行业
        if(ListUtil.isNotEmpty(quitEvent.getIndustryIds())){
            queryBuilder.must(QueryBuilders.termsQuery("industryIds",quitEvent.getIndustryIds()));
        }
        //按退出方式
        if(ListUtil.isNotEmpty(quitEvent.getQuitTypes())){
            queryBuilder.must(QueryBuilders.termsQuery("quitType",quitEvent.getQuitTypes()));
        }
        //按币种
        if(ListUtil.isNotEmpty(quitEvent.getCurrencys())){
            queryBuilder.must(QueryBuilders.termsQuery("currencyTitle",quitEvent.getCurrencys()));
        }
        //按title
        if(!StringUtils.isEmpty(quitEvent.getCompany())){
            queryBuilder.should(QueryBuilders.wildcardQuery("projTitle","*"+quitEvent.getCompany()+"*"));
            queryBuilder.should(QueryBuilders.wildcardQuery("mergeSideJson","*"+quitEvent.getQuitSideJson()+"*"));
        }
        //按退出时间
        if(!StringUtils.isEmpty(quitEvent.getStartDate()) || !StringUtils.isEmpty(quitEvent.getEndDate())){
            RangeQueryBuilder rangeq = QueryBuilders.rangeQuery("quitDate");
            if(!StringUtils.isEmpty(quitEvent.getStartDate()) ){
                rangeq.gte(quitEvent.getStartDate());
            }
            if(!StringUtils.isEmpty(quitEvent.getEndDate())){
                rangeq.lte(quitEvent.getEndDate());
            }
            queryBuilder.filter(rangeq);
        }
        //按地区
        if(ListUtil.isNotEmpty(quitEvent.getDistrictIds())){
            queryBuilder.should(QueryBuilders.termsQuery("districtId",quitEvent.getDistrictIds()));
        }
        if(ListUtil.isNotEmpty(quitEvent.getDistrictSubIds())){
            queryBuilder.should(QueryBuilders.termsQuery("districtSubId",quitEvent.getDistrictSubIds()));
        }
        //设置分页参数和请求参数
        SearchRequestBuilder sb = client.prepareSearch(INDEX);
        sb.setQuery(queryBuilder);
        //求总数
        SearchResponse res =sb.setTypes(TYPE).setSearchType(SearchType.DEFAULT).execute().actionGet();
        Long  totalHit = res.getHits().totalHits();

        if(!StringUtils.isEmpty(quitEvent.getOrderBy())){
            sb.addSort(quitEvent.getOrderBy(), SortOrder.fromString(quitEvent.getOrder()));
        }else {
            sb.addSort("quitDate", SortOrder.DESC);
        }
        sb.setFrom(pageNum*pageSize).setSize(pageSize);
        //返回响应
        SearchResponse response =sb.setTypes(TYPE).setSearchType(SearchType.DEFAULT).execute().actionGet();
        SearchHits shs = response.getHits();
        List<Object> entityList = new ArrayList<>();
        for (SearchHit it : shs) {
            Map source = it.getSource();
            QuitEventQuery entity =  JSON.parseObject(JSON.toJSONString(source),QuitEventQuery.class);
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
                    String rep = "<comp>"+entity.getCompany()+"</comp>";
                    //替换
                    field.set(entity, value.replaceAll(entity.getCompany(),rep));
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