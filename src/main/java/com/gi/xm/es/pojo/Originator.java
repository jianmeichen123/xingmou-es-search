package com.gi.xm.es.pojo;

import java.io.Serializable;

/**
 * Created by zcy on 16-12-13.
 * 创始人 Elasticsearch 实体类
 * @author zhangchunyuan
 */
public class Originator implements Serializable{

    /**
     * 创始人姓名
     */
    private String name ;
    /**
     * 当前公司
     */
    private String projectName;
    /**
     * 当期职位
     */
    private String position;
    /**
     * 学校
     */
    private String schoolNames;
    /**
     * 工作经历
     */
    private String jobDescription;
    /**
     * 创始人头像
     */
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSchoolNames() {
        return schoolNames;
    }

    public void setSchoolNames(String schoolNames) {
        this.schoolNames = schoolNames;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobDescription() {
        return jobDescription;
    }
}
