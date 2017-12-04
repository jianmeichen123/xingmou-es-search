package com.gi.xm.es.view;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author zhangchunyuan
 *         查询分页对象
 */
public class Pagination implements Serializable {


    private static final long serialVersionUID = 1L;

    private Integer pageSize =10;

    private Integer pageNo =0;

    private Long total;

    private List<Object> records;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Object> getRecords() {
        return records;
    }

    public void setRecords(List<Object> records) {
        this.records = records;
    }
}
