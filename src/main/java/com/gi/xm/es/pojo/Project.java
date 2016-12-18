package com.gi.xm.es.pojo;

import java.io.Serializable;


/**
 * Created by zcy on 16-12-13
 * 项目 Elasticsearch 实体类
 * @author zhangchunyuan
 */
public class Project implements Serializable  {

    /**
     * 项目id
     */
    private Long sid;
    /**
     * 标题
     */
    private String title;
    /**
     *详情
     */
    private String description;
    /**
     * 项目logo
     */
    private String logo;
    /**
     * 行业图标
     */
    private String icon;
    /**
     * 一级行业
     */
    private String indudstryName;
    /**
     * 二级行业
     */
    private String indudstrySubName;
    /**
     * 标签
     */
    private String labels;
    /**
     *轮次
     */
    private String roundName;
    /**
     * 成立日期
     */
    private String createDate;

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIndudstryName() {
        return indudstryName;
    }

    public void setIndudstryName(String indudstryName) {
        this.indudstryName = indudstryName;
    }

    public String getIndudstrySubName() {
        return indudstrySubName;
    }

    public void setIndudstrySubName(String indudstrySubName) {
        this.indudstrySubName = indudstrySubName;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getLabels() {
        return labels;
    }
}
