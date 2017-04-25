package com.gi.xm.es.controller;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.pojo.query.ProjectQuery;
import com.gi.xm.es.service.UserLogService;
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
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/search/")
public class ProjectController {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private Client client;

    @Autowired
    private UserLogService userLogService;

    private static final Integer SEARCHLIMIT = 2000;

    private final String INDEX = "ctdn_project";

    private final String TYPE = "project";

    private static Result errorRet = new Result(MessageStatus.MISS_PARAMETER.getMessage(), MessageStatus.MISS_PARAMETER.getStatus());

    @RequestMapping(value="project",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result queryProject(@RequestBody ProjectQuery project) {
        Result ret = new Result();
        Integer pageSize = project.getPageSize();
        Integer pageNum = project.getPageNo();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        SearchRequestBuilder sb = client.prepareSearch(INDEX);
        //按行业
        if(project.getIndustryIds()!= null && !project.getIndustryIds().isEmpty() ){
            queryBuilder.must(QueryBuilders.termsQuery("industryIds",project.getIndustryIds()));
        }
        //按title
        if(project.getProjTitle() != null){
            queryBuilder.must(QueryBuilders.wildcardQuery("projTitle","*"+project.getProjTitle() +"*"));
            //设置高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("projTitle");
            sb.highlighter(highlightBuilder);
        }
        //按createDate
        if(project.getStartDate() != null || project.getEndDate() != null){
            RangeQueryBuilder rangeq = QueryBuilders.rangeQuery("setupDT");
            if(project.getStartDate() != null ){
                rangeq.gte(project.getStartDate());
            }
            if(project.getEndDate() != null ){
                rangeq.lte(project.getEndDate());
            }
            queryBuilder.filter(rangeq);
        }
        //按round
        if(project.getRoundList()  != null && !project.getRoundList().isEmpty() ){
            queryBuilder.must(QueryBuilders.termsQuery("latestFinanceRound",project.getRoundList()));
        }
        //按地区
        if(project.getDistrictIds() != null && !project.getDistrictIds().isEmpty()){
            queryBuilder.must(QueryBuilders.termsQuery("districtId",project.getDistrictIds()));
        }
        if(project.getDistrictSubIds() != null && !project.getDistrictSubIds().isEmpty()){
            queryBuilder.must(QueryBuilders.termsQuery("districtSubId",project.getDistrictSubIds()));
        }
        sb.setQuery(queryBuilder);
        //求总数
        SearchResponse res =sb.setTypes(TYPE).setSearchType(SearchType.DEFAULT).execute().actionGet();
        Long  totalHit = res.getHits().totalHits();
        //排序
        if(project.getOrder()!= null){
            sb.addSort(project.getOrder(), SortOrder.fromString(project.getOrderBy()));
        }else{
            sb.addSort("loadDate", SortOrder.DESC);
        }
        //设置分页参数和请求参数
        sb.setFrom(pageNum);
        sb.setSize(pageSize);
        //返回响应
        SearchResponse response =sb.setTypes(TYPE).setSearchType(SearchType.DEFAULT).execute().actionGet();
        SearchHits shs = response.getHits();
        List<Object> entityList = new ArrayList<>();
        for (SearchHit it : shs) {
            Map source = it.getSource();
            ProjectQuery p =  JSON.parseObject(JSON.toJSONString(source),ProjectQuery.class);
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
                    String rep = "<span class = 'highlight'>"+project.getProjTitle()+"</span>";
                    //替换
                    field.set(p, value.replaceAll(project.getProjTitle(),rep));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            entityList.add(p);
        }
        Pagination page = new Pagination();
        page.setTotal(totalHit>SEARCHLIMIT?SEARCHLIMIT:totalHit);
        page.setTotalhit(totalHit);
        page.setRecords(entityList);
        ret = new Result(MessageStatus.OK.getMessage(), MessageStatus.OK.getStatus(), page);
        return ret;
    }
}