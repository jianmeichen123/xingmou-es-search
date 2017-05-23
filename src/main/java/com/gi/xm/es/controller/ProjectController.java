package com.gi.xm.es.controller;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.pojo.query.ProjectQuery;
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
public class ProjectController {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private Client client;

    private static final Integer SEARCHLIMIT = 2000;

    private final String INDEX = "ctdn_project";

    private final String TYPE = "project";

    private static Result errorRet = new Result(MessageStatus.MISS_PARAMETER.getMessage(), MessageStatus.MISS_PARAMETER.getStatus());

    @RequestMapping(value = "project", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result queryProject(@RequestBody ProjectQuery project) {
        Result ret = new Result();
        Integer pageSize = project.getPageSize();
        Integer pageNum = project.getPageNo();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        SearchRequestBuilder sb = client.prepareSearch(INDEX);
        //按行业
        if (ListUtil.isNotEmpty(project.getIndustryIds())) {
            queryBuilder.must(QueryBuilders.termsQuery("industryIds", project.getIndustryIds()));
        }
        //按title
        if (!StringUtils.isEmpty(project.getProjTitle())) {
            project.setProjTitle(project.getProjTitle().trim());
            queryBuilder.must(QueryBuilders.wildcardQuery("projTitle", "*" + project.getProjTitle() + "*"));
            //设置高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("projTitle");
            sb.highlighter(highlightBuilder);
        }
        //按createDate
        if (!StringUtils.isEmpty(project.getStartDate())  || !StringUtils.isEmpty(project.getEndDate())) {
            RangeQueryBuilder rangeq = QueryBuilders.rangeQuery("setupDT");
            if (!StringUtils.isEmpty(project.getStartDate())) {
                rangeq.gte(project.getStartDate());
            }
            if (!StringUtils.isEmpty(project.getEndDate())) {
                rangeq.lte(project.getEndDate());
            }
            queryBuilder.filter(rangeq);
        }
        //按round
        if (ListUtil.isNotEmpty(project.getRounds())) {
            queryBuilder.must(QueryBuilders.termsQuery("latestFinanceRound", project.getRounds()));
        }
        //按地区
        if (ListUtil.isNotEmpty(project.getDistrictIds())&&ListUtil.isNotEmpty(project.getDistrictSubIds())) {
            BoolQueryBuilder shoudBuilder = QueryBuilders.boolQuery();
            shoudBuilder.should(QueryBuilders.termsQuery("districtId", project.getDistrictIds()));
            shoudBuilder.should(QueryBuilders.termsQuery("districtSubId", project.getDistrictSubIds()));
            shoudBuilder.minimumNumberShouldMatch(1);
            queryBuilder.must(shoudBuilder);
        }else if (ListUtil.isNotEmpty(project.getDistrictIds())) {
            queryBuilder.must(QueryBuilders.termsQuery("districtId", project.getDistrictIds()));
        }else if (ListUtil.isNotEmpty(project.getDistrictSubIds())) {
            queryBuilder.must(QueryBuilders.termsQuery("districtSubId", project.getDistrictSubIds()));
        }
        sb.setQuery(queryBuilder);
        //求总数
        SearchResponse res = sb.setTypes(TYPE).setSearchType(SearchType.DEFAULT).execute().actionGet();
        Long totalHit = res.getHits().totalHits();
        //排序
        if (project.getOrderBy() != null) {
            sb.addSort(project.getOrderBy(), SortOrder.fromString(project.getOrder()));
        } else {
            sb.addSort("loadDate", SortOrder.DESC);
        }
        //设置分页参数和请求参数
        Integer tmp = pageSize;
        if (pageSize*pageNum+pageSize > SEARCHLIMIT){
            tmp =  SEARCHLIMIT - pageSize*pageNum;
        }
        sb.setFrom(pageNum*pageSize).setSize(tmp);
        //返回响应
        SearchResponse response = sb.setTypes(TYPE).setSearchType(SearchType.DEFAULT).execute().actionGet();
        SearchHits shs = response.getHits();
        List<Object> entityList = new ArrayList<>();
        for (SearchHit it : shs) {
            Map source = it.getSource();
            ProjectQuery p = JSON.parseObject(JSON.toJSONString(source), ProjectQuery.class);
            //获取对应的高亮域
            Map<String, HighlightField> result = it.highlightFields();
            //从设定的高亮域中取得指定域
            for (Map.Entry<String, HighlightField> entry : result.entrySet()) {
                String key = entry.getKey();
                try {
                    //获得高亮字段的原值
                    Field field = p.getClass().getDeclaredField(key);
                    field.setAccessible(true);
                    String value = field.get(p).toString();
                    //获得搜索关键字
                    String rep = "<comp>" + project.getProjTitle() + "</comp>";
                    //替换
                    field.set(p, value.replaceAll(project.getProjTitle(), rep));
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                    return errorRet;
                }
            }
            entityList.add(p);
        }
        Pagination page = new Pagination();
        page.setTotal(totalHit > SEARCHLIMIT ? SEARCHLIMIT : totalHit);
        page.setTotalhit(totalHit);
        page.setRecords(entityList);
        ret = new Result(MessageStatus.OK.getMessage(), MessageStatus.OK.getStatus(), page);
        return ret;
    }
}