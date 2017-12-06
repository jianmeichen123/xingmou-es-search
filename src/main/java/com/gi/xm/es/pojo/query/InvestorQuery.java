package com.gi.xm.es.pojo.query;

import com.gi.xm.es.pojo.Query;

import java.io.Serializable;

public class InvestorQuery extends Query implements Serializable  {
	private Long id;
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
	 * 职位
	 */
	private String job;
	/**
	 * 投领域/行业
	 */
	private String fields;
	/**
	 * 投资阶段/轮次
	 */
	private String rounds;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getZhName() {
		return zhName;
	}

	public void setZhName(String zhName) {
		this.zhName = zhName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getRounds() {
		return rounds;
	}

	public void setRounds(String rounds) {
		this.rounds = rounds;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getDistrictSubName() {
		return districtSubName;
	}

	public void setDistrictSubName(String districtSubName) {
		this.districtSubName = districtSubName;
	}

	public String getDistrictGrandsonName() {
		return districtGrandsonName;
	}

	public void setDistrictGrandsonName(String districtGrandsonName) {
		this.districtGrandsonName = districtGrandsonName;
	}
}