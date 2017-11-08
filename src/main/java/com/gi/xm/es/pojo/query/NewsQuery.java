package com.gi.xm.es.pojo.query;

import com.gi.xm.es.pojo.Query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by zcy on 17-4-18.
 */
public class NewsQuery extends Query implements Serializable {
    private Long newsId;

    private String objCode;

    private String  objType;

    private String newsReportDate;

    private String newsTitle;

    private String newsOverview;

    private String labels;

    private String newsContent;

    private String newsListpics;

    private String newsSource;

    private String newsAddress;

    private Integer newsType;

    private String newsTypeName;

    private Long newsReportTime;

    private Integer newsColumns;

    private String order;

    private String orderBy;

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public String getObjCode() {
        return objCode;
    }

    public void setObjCode(String objCode) {
        this.objCode = objCode;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public void setNewsReportDate(String newsReportDate) {
        this.newsReportDate = newsReportDate;
    }

    public String getNewsReportDate() {
        return newsReportDate;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsOverview() {
        return newsOverview;
    }

    public void setNewsOverview(String newsOverview) {
        this.newsOverview = newsOverview;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsListpics() {
        return newsListpics;
    }

    public void setNewsListpics(String newsListpics) {
        this.newsListpics = newsListpics;
    }

    public String getNewsSource() {
        return newsSource;
    }

    public void setNewsSource(String newsSource) {
        this.newsSource = newsSource;
    }

    public String getNewsAddress() {
        return newsAddress;
    }

    public void setNewsAddress(String newsAddress) {
        this.newsAddress = newsAddress;
    }

    public Integer getNewsType() {
        return newsType;
    }

    public void setNewsType(Integer newsType) {
        this.newsType = newsType;
    }

    public String getNewsTypeName() {
        return newsTypeName;
    }

    public void setNewsTypeName(String newsTypeName) {
        this.newsTypeName = newsTypeName;
    }

    public Long getNewsReportTime() {
        return newsReportTime;
    }

    public void setNewsReportTime(Long newsReportTime) {
        this.newsReportTime = newsReportTime;
    }

    public Integer getNewsColumns() {
        return newsColumns;
    }

    public void setNewsColumns(Integer newsColumns) {
        this.newsColumns = newsColumns;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrder() {
        return order;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderBy() {
        return orderBy;
    }
}
