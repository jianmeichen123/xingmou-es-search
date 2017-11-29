package com.gi.xm.es.pojo.query;

import com.gi.xm.es.pojo.Pagination;
import com.gi.xm.es.pojo.Query;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zcy on 17-4-18.
 */
public class InvestEventQuery extends Query implements Serializable {

    private Long eventId;

    private Long sourceId;

    private String sourceCode;

    private String  industryName;

    private String  industrySubName;

    private String round;

    private String districtSubName;

    private String company;

    private String investDate;

    private String amountStr;

    private Long amountNum;

    private String currencyType;

    private String investSideJson;

    private List<Long> industryIds;

    private List<String> investRounds;

    private List<Long> districtIds;

    private List<Long> districtSubIds;

    private List<String> currencyTypes;

    private String startDate;

    private String endDate;

    private String order;

    private String orderBy;

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getSourceId() {
        return sourceId;
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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
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

    public List<Long> getIndustryIds() {
        return industryIds;
    }

    public void setIndustryIds(List<Long> industryIds) {
        this.industryIds = industryIds;
    }
}
