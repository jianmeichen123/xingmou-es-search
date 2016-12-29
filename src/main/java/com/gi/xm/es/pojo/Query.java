package com.gi.xm.es.pojo;

/**
 * Created by zcy on 16-12-8.
 */
public class Query  extends com.gi.xm.es.view.Pagination {

    /**
     * 搜索字段
     */
    private String keyword ;

    /**
     * 搜索类型
     * @param category
     */
    private String category ;


    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

}
