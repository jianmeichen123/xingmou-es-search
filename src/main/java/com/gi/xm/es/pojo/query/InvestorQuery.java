package com.gi.xm.es.pojo.query;

import com.gi.xm.es.pojo.Query;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

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
	 * 年龄
	 */
	private Integer age;
	/**
	 * 性别
	 */
	private String gender;
	/**
	 * 工作年限
	 */
	private String workLife;

	/**
	 * 公司code
	 */
	private String projCode;

	/**
	 * 所在公司
	 */
	private String projTitle;
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
	private List<String> rounds;
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


	private String colleage;

	private String degree;

	private String url;

	@ApiModelProperty(value = "一级行业id集合,用于行业筛选")
	private List<String> industryIds;

	@ApiModelProperty(value = "轮次集合,用于轮次筛选")
	private List<String> InvestorRounds;

	@ApiModelProperty(value = "一级地区id集合,用于一级地区筛选")
	private List<Long> districtIds;

	@ApiModelProperty(value = "二级地区id集合,用于二级地区筛选")
	private List<Long> districtSubIds;

	private Integer showOrder;

	@ApiModelProperty(value = "升序:asc 降序:desc")
	private String order;

	@ApiModelProperty(value = "排序字段")
	private String orderBy;

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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getWorkLife() {
		return workLife;
	}

	public void setWorkLife(String workLife) {
		this.workLife = workLife;
	}

	public String getProjCode() {
		return projCode;
	}

	public void setProjCode(String projCode) {
		this.projCode = projCode;
	}

	public String getProjTitle() {
		return projTitle;
	}

	public void setProjTitle(String projTitle) {
		this.projTitle = projTitle;
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

	public List<String> getIndustryIds() {
		return industryIds;
	}

	public void setIndustryIds(List<String> industryIds) {
		this.industryIds = industryIds;
	}

	public List<Long> getDistrictIds() {
		return districtIds;
	}

	public void setDistrictIds(List<Long> districtIds) {
		this.districtIds = districtIds;
	}

	public List<Long> getDistrictSubIds() {
		return districtSubIds;
	}

	public void setDistrictSubIds(List<Long> districtSubIds) {
		this.districtSubIds = districtSubIds;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}


	public String getColleage() {
		return colleage;
	}

	public void setColleage(String colleage) {
		this.colleage = colleage;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getInvestorRounds() {
		return InvestorRounds;
	}

	public void setInvestorRounds(List<String> investorRounds) {
		InvestorRounds = investorRounds;
	}

	public List<String> getRounds() {
		return rounds;
	}

	public void setRounds(List<String> rounds) {
		this.rounds = rounds;
	}
}