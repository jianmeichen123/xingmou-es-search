package com.gi.xm.es.pojo.query;

import com.gi.xm.es.view.Pagination;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zcy on 17-4-21.
 */
public class QuitEventQuery extends Pagination implements Serializable {

    private Long eventId;

    private String code;

    private Long sourceId;

    private Long sourceCode;

    private String  industryName;

    private String  industrySubName;

    private String quitType;

    private String district;

    private String logo;

    private String company;

    private String quitDate;

    private String startDate;

    private String endDate;

    private String quitAmountStr;

    private Integer quitAmountNum;

    private String currencyTitle;

    private String quitSideJson;

    private List<String> industryIds;

    private List<String> quitTypes;

    private List<String> currencys;

    private List<Long> districtIds;

    private List<Long> districtSubIds;

    private String order;

    private String orderBy;

    private String bodyRole;

    private String sourceType;

    private String isClick;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getQuitType() {
        return quitType;
    }

    public void setQuitType(String quitType) {
        this.quitType = quitType;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getQuitDate() {
        return quitDate;
    }

    public void setQuitDate(String quitDate) {
        this.quitDate = quitDate;
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

    public String getQuitAmountStr() {
        return quitAmountStr;
    }

    public void setQuitAmountStr(String quitAmountStr) {
        this.quitAmountStr = quitAmountStr;
    }

    public Integer getQuitAmountNum() {
        return quitAmountNum;
    }

    public void setQuitAmountNum(Integer quitAmountNum) {
        this.quitAmountNum = quitAmountNum;
    }

    public String getCurrencyTitle() {
        return currencyTitle;
    }

    public void setCurrencyTitle(String currencyTitle) {
        this.currencyTitle = currencyTitle;
    }

    public String getQuitSideJson() {
        return quitSideJson;
    }

    public void setQuitSideJson(String quitSideJson) {
        this.quitSideJson = quitSideJson;
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

    public List<String> getCurrencys() {
        return currencys;
    }

    public void setCurrencys(List<String> currencys) {
        this.currencys = currencys;
    }

    public List<String> getQuitTypes() {
        return quitTypes;
    }

    public void setQuitTypes(List<String> quitTypes) {
        this.quitTypes = quitTypes;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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
