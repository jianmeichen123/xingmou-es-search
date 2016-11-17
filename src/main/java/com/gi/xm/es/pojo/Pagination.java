package com.gi.xm.es.pojo;

import java.io.Serializable;
import java.util.List;

public class Pagination implements Serializable {
	
	private Integer pageSize  = 10;
	
	private Integer pageNo = 1;
	
	private Integer totalCount;
	
	private List<Search> list;

	public Pagination(){ 
	}
	public Integer getPageSize() {
		return pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<Search> getList() {
		return list;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public void setList(List<Search> list) {
		this.list = list;
	}
	
	

}
