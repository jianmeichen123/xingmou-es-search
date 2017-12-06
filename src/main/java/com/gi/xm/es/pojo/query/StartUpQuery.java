package com.gi.xm.es.pojo.query;

import com.gi.xm.es.pojo.Query;

import java.io.Serializable;

public class StartUpQuery extends Query implements Serializable  {
	private String id;
	/**
	 * 创业者code
	 */
	private String code;
	/**
     * 中文名
	 */
	private String zhName;
	/**
     * 英文名
	 */
	private String enName;
	/**
     * 所在公司
	 */
	private String company;
	/**
     * 个人简介
	 */
	private String introduce;
	/**
     * 所属职位
	 */
	private String job;
	/**
     * 创业领域
	 */
	private String fields;
	/**
     * 一级地区
	 */
	private String districtName;
	/**
     * 二级地区
	 */
	private String districtSubName;
	/**
     * 三级地区
	 */
	private String districtGrandsonName;
}