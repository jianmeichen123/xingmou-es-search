package com.gi.xm.es.service;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.pojo.Query;
import com.gi.xm.es.pojo.query.ProjectQuery;
import com.gi.xm.es.util.ListUtil;
import org.apache.lucene.queryparser.classic.QueryParserBase;
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
public class ProjectService extends BaseService {

    @Autowired
    private Client client;


    @Value("${ctdn.project.index}")
    private  String index;

    @Value("${ctdn.project.type}")
    private  String type;

    /**构建请求体:
     *全文搜索查符合条件项目数
     * @return
     */
    public  Long queryNum(Query query){
        ProjectQuery project = new ProjectQuery();
        project.setKeyword(query.getKeyword());
        SearchRequestBuilder qb = queryList(project);
        SearchHits ssh = getSearchHits(qb);
        return ssh.getTotalHits();
    }

    /**构建请求体:
     * 按多条件查询项目列表
     * @param project
     * @return
     */
    public SearchRequestBuilder queryList(ProjectQuery project){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        SearchRequestBuilder srb = client.prepareSearch(index);
        //按行业
        if (ListUtil.isNotEmpty(project.getIndustryIds())) {
            queryBuilder.must(QueryBuilders.termsQuery("industryIds", project.getIndustryIds()));
        }
        //按title
        if (!StringUtils.isEmpty(project.getKeyword())) {
            project.setKeyword(QueryParserBase.escape(project.getKeyword().trim()));
            queryBuilder.must(QueryBuilders.wildcardQuery("projTitle", "*" + project.getKeyword() + "*"));
            //设置高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("projTitle");
            srb.highlighter(highlightBuilder);
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
        srb.setQuery(queryBuilder);
        //求总数
        SearchHits  shs = getSearchHits(srb);
        Long totalHit = shs.getTotalHits();
        //排序
        if (project.getOrderBy() != null) {
            srb.addSort(project.getOrderBy(), SortOrder.fromString(project.getOrder()));
        } else {
            srb.addSort("showOrder", SortOrder.ASC);
        }
        //设置分页参数和请求参数
        Integer tmp = project.getPageSize();
        Integer pageSize = project.getPageSize();
        Integer pageNo = project.getPageNo();
        if (pageSize*pageNo+pageSize > max_search_result){
            tmp =  max_search_result - pageSize*pageNo;
        }
        srb.setFrom(pageNo*pageSize).setSize(tmp);
        return srb;
    }

    /**
     * 查询结果转List
     * @param project
     * @param shs
     * @return
     * @throws Exception
     */
    public  List<Object> getResponseList (ProjectQuery project,SearchHits shs)  throws Exception{
        List<Object> entityList = new ArrayList<>();
        for (SearchHit it : shs) {
            Map source = it.getSource();
            ProjectQuery p = JSON.parseObject(JSON.toJSONString(source), ProjectQuery.class);
            //获取对应的高亮域
            Map<String, HighlightField> result = it.highlightFields();
            if(result != null){
                //从设定的高亮域中取得指定域
                try{
                    for (Map.Entry<String, HighlightField> entry : result.entrySet()) {
                        String key = entry.getKey();
                        //获得高亮字段的原值
                        Field field = p.getClass().getDeclaredField(key);
                        field.setAccessible(true);
                        String value = field.get(p).toString();
                        //获得搜索关键字
                        String rep = "<comp>" + project.getKeyword() + "</comp>";
                        //替换
                        field.set(p, value.replaceAll(project.getKeyword(), rep));
                    }
                } catch (Exception e) {
                    throw e;
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
}
