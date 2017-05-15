package com.gi.xm.es.pojo.query;

import com.gi.xm.es.pojo.Pagination;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zcy on 17-4-18.
 */
public class InvestEventQuery extends Pagination implements Serializable {

    private Long id;

    private String code;

    private Long sourceId;

    private Long sourceCode;

    private String  industryName;

    private String  industrySubName;

    private String round;

    private String districtSubName;

    private String logo;

    private String company;

    private String investDate;

    private String amountStr;

    private Long amountNum;

    private String currencyType;

    private String investSideJson;

    private List<String> industrys;

    private List<String> investRounds;

    private List<Long> districtIds;

    private List<Long> districtSubIds;

    private List<String> currencyTypes;

    private String startDate;

    private String endDate;

    private String order;

    private String orderBy;

    private String bodyRole;

    private String sourceType;

    private String isClick;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public List<String> getIndustrys() {
        return industrys;
    }

    public void setIndustrys(List<String> industrys) {
        this.industrys = industrys;
    }

    public List<String> getInvestRounds() {
        return investRounds;
    }

    public void setInvestRounds(List<String> investRounds) {
        this.investRounds = investRounds;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public List<String> getCurrencyTypes() {
        return currencyTypes;
    }

    public void setCurrencyTypes(List<String> currencyTypes) {
        this.currencyTypes = currencyTypes;
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

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrder() {
        return order;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getDistrictSubName() {
        return districtSubName;
    }

    public void setDistrictSubName(String districtSubName) {
        this.districtSubName = districtSubName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public void setInvestDate(String investDate) {
        this.investDate = investDate;
    }

    public String getInvestDate() {
        return investDate;
    }

    public String getAmountStr() {
        return amountStr;
    }

    public void setAmountStr(String amountStr) {
        this.amountStr = amountStr;
    }

    public Long getAmountNum() {
        return amountNum;
    }

    public void setAmountNum(Long amountNum) {
        this.amountNum = amountNum;
    }

    public String getInvestSideJson() {
        return investSideJson;
    }

    public void setInvestSideJson(String investSideJson) {
        this.investSideJson = investSideJson;
    }

    public String getBodyRole() {
        return bodyRole;
    }

    public void setBodyRole(String bodyRole) {
        this.bodyRole = bodyRole;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getIsClick() {
        return isClick;
    }

    public void setIsClick(String isClick) {
        this.isClick = isClick;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(Long sourceCode) {
        this.sourceCode = sourceCode;
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
}
