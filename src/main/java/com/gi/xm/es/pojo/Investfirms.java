package com.gi.xm.es.pojo;

import java.io.Serializable;

/**
 * Created by zcy on 16-12-13.
 * 投资机构 Elasticsearch 实体类
 * @author zhangchunyuan
 */
public class Investfirms implements Serializable{

    /**
     * 机构名称
     */
    private String name;
    /**
     * 简介
     */
    private String  description;
    /**
     * 关注领域
     */
    private String investIndustry;
    /**
     * 投资轮次
     */
    private String roundNames;
    /**
     * 最近投资项目

     */
    private String recentProjects;
    /**
     * 投资项目id
     */
    private String projectIds;
    /**
     *
     * 投资机构id
     */
    private Long sid;

    /**
     * 投资机构logo
     * @return
     */
    private String logo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInvestIndustry(String investIndustry) {
        this.investIndustry = investIndustry;
    }

    public String getInvestIndustry() {
        return investIndustry;
    }

    public String getRoundNames() {
        return roundNames;
    }

    public void setRoundNames(String roundNames) {
        this.roundNames = roundNames;
    }

    public String getRecentProjects() {
        return recentProjects;
    }

    public void setRecentProjects(String recentProjects) {
        this.recentProjects = recentProjects;
    }

    public String getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(String projectIds) {
        this.projectIds = projectIds;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}
