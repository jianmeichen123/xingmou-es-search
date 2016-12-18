package com.gi.xm.es.pojo;

import java.io.Serializable;

/**
 * Created by zcy on 16-12-13.
 * 投资人 Elasticsearch 实体类
 * @author zhangchunyuan
 */
public class Investor implements Serializable{

    /**
     * 投资人姓名
     */
    private String name ;
    /**
     * 所属机构
     */
    private String investfirmName;
    /**
     * 当前职位
     */
    private String position;
    /**
     * 个人简介
     */
    private String desciption;
    /**
     * 投资人id
     */
    private Long sid;
    /**
     * 投资人头像
     */
    private  String avatar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInvestfirmName(String investfirmName) {
        this.investfirmName = investfirmName;
    }

    public String getInvestfirmName() {
        return investfirmName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
