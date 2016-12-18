package com.gi.xm.es.view;

import com.gi.xm.es.pojo.Search;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author zhangchunyuan
 *         查询分页对象
 */
public class Pagination implements Serializable {


    private static final long serialVersionUID = 1L;

    private Integer pageSize ;

    private Integer pageNo ;
    /**
     * 每种分类查询总数
     */
    private Long total;

    /**
     * 总命中条数
     * @param totalhit
     */
    private Long totalhit;

    private List<Object> records;

    private Map<String, Long> map;

    public Map<String, Long> getMap() {
        return map;
    }

    public void setMap(Map<String, Long> map) {
        this.map = map;
    }

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

    public Long getTotalhit() {
        return totalhit;
    }

    public void setTotalhit(Long totalhit) {
        this.totalhit = totalhit;
    }

    public List<Object> getRecords() {
        return records;
    }

    public void setRecords(List<Object> records) {
        this.records = records;
    }
}
