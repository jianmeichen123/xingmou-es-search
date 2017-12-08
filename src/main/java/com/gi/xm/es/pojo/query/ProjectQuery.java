package com.gi.xm.es.pojo.query;

import com.gi.xm.es.pojo.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zcy on 17-1-17.
 */
@ApiModel
public class ProjectQuery extends Query implements Serializable{

    @ApiModelProperty(value = "项目code 项目图片地址:http://static.galaxyinternet.com/img/project/+projCode+.png")
    private String projCode;

    @ApiModelProperty(value = "项目标题")
    private String projTitle;

    @ApiModelProperty(value = "项目slogan")
    private String introduce;

    @ApiModelProperty(value = "项目一级行业")
    private String industryName;

    @ApiModelProperty(value = "项目二级行业")
    private String industrySubName;

    @ApiModelProperty(value = "最新融资轮次")
    private String latestFinanceRound;

    @ApiModelProperty(value = "二级地区")
    private String districtSubName;

    @ApiModelProperty(value = "成立时间")
    private String  setupDT;

    @ApiModelProperty(value = "最新融资时间")
    private String latestFinanceDT;

    @ApiModelProperty(value = "最新融资金额")
    private String  latestFinanceAmountStr;

    @ApiModelProperty(value = "一级行业id集合,用于行业筛选")
    private List<String> industryIds;

    @ApiModelProperty(value = "轮次集合,用于轮次筛选")
    private List<String> rounds;

    @ApiModelProperty(value = "一级地区id集合,用于一级地区筛选")
    private List<Long> districtIds;

    @ApiModelProperty(value = "二级地区id集合,用于二级地区筛选")
    private List<Long> districtSubIds;

    @ApiModelProperty(value = "开始时间,用于按项目成立时间筛选")
    private String startDate;

    @ApiModelProperty(value = "结束时间,用于按项目成立时间筛选")
    private String endDate;

    private Integer showOrder;

    @ApiModelProperty(value = "升序:asc 降序:desc")
    private String order;

    @ApiModelProperty(value = "排序字段")
    private String orderBy;

    public String getProjCode() {
        return projCode;
    }

    public void setProjCode(String projCode) {
        this.projCode = projCode;
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

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getIndustrySubName() {
        return industrySubName;
    }

    public void setIndustrySubName(String industrySubName) {
        this.industrySubName = industrySubName;
    }

    public String getProjTitle() {
        return projTitle;
    }

    public void setProjTitle(String projTitle) {
        this.projTitle = projTitle;
    }

    public String getLatestFinanceRound() {
        return latestFinanceRound;
    }

    public void setLatestFinanceRound(String latestFinanceRound) {
        this.latestFinanceRound = latestFinanceRound;
    }

    public String getDistrictSubName() {
        return districtSubName;
    }

    public void setDistrictSubName(String districtSubName) {
        this.districtSubName = districtSubName;
    }

    public String getSetupDT() {
        return setupDT;
    }

    public void setSetupDT(String setupDT) {
        this.setupDT = setupDT;
    }

    public String getLatestFinanceDT() {
        return latestFinanceDT;
    }

    public void setLatestFinanceDT(String latestFinanceDT) {
        this.latestFinanceDT = latestFinanceDT;
    }

    public String getLatestFinanceAmountStr() {
        return latestFinanceAmountStr;
    }

    public void setLatestFinanceAmountStr(String latestFinanceAmountStr) {
        this.latestFinanceAmountStr = latestFinanceAmountStr;
    }
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public Integer getShowOrder() {
        return showOrder;
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

    public void setIndustryIds(List<String> industryIds) {
        this.industryIds = industryIds;
    }

    public List<String> getIndustryIds() {
        return industryIds;
    }

    public List<String> getRounds() {
        return rounds;
    }

    public void setRounds(List<String> rounds) {
        this.rounds = rounds;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getIntroduce() {
        return introduce;
    }
}
