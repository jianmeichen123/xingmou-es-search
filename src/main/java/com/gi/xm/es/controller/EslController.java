package com.gi.xm.es.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gi.xm.es.pojo.Pagination;
import com.gi.xm.es.pojo.Search;
import com.gi.xm.es.util.CreateIndex;
import net.sf.json.JSONObject;

@RestController
@RequestMapping(value = "/es/")
public class EslController {

	private static final Logger LOG = LoggerFactory.getLogger(EslController.class);

	/**
	 * 分页查询 @author zhangchunyuan
	 * 
	 * @param keyword
	 *            查询字段，匹配查询 title字段和body字段
	 * @param pageSize
	 *            每页记录数
	 * @param pageNo
	 *            当前页码
	 * @return Pagination
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("searchBypage")
	@ResponseBody
	public  Pagination findByPage(String keyword, Integer pageSize, Integer pageNo) {
		Pagination page = new Pagination();
		pageNo = null == pageNo ? 1 : pageNo;
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);

		Client client = null;
		try {
			   client = new PreBuiltTransportClient(Settings.EMPTY)
				        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.9.130.135"), 9300));
			} catch (UnknownHostException e) {
				e.printStackTrace();
		}
		
		QueryBuilder queryBuilder = QueryBuilders.disMaxQuery().add(QueryBuilders.termQuery("title", keyword))
				.add(QueryBuilders.matchQuery("body", keyword).analyzer("ik"));
		
		//设置分页参数
		SearchRequestBuilder srb = client.prepareSearch(CreateIndex.INDEX);
		srb.setQuery(queryBuilder);
		srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		
		srb.setFrom((pageNo - 1) * pageSize).setSize(pageSize).setExplain(true);
		
		SearchResponse response = srb.execute().actionGet();
		SearchHits hits = response.getHits();
		page.setTotalCount((int) hits.getTotalHits());
		List<Search> list = new ArrayList<Search>();
		for (SearchHit searchHit : hits) {
			Map source = searchHit.getSource();
			Search entity = (Search) JSONObject.toBean(JSONObject.fromObject(source), Search.class);
			list.add(entity);
		}
		page.setList(list);
		//记录日志
		LOG.info(pageNo+":"+keyword);
		return page;
	}

}