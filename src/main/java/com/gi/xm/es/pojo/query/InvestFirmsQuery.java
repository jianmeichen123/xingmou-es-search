package com.gi.xm.es.pojo.query;

import com.gi.xm.es.pojo.Pagination;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zcy on 17-4-24.
 */
public class InvestFirmsQuery extends Pagination implements Serializable {

    private String code;

    private Long sourceId;

    private String focusDomain;

    private String investStage;

    private String orgType;

    private String capitalType;

    private String currencyTitle;

    private String logo;

    private String orgName;

    private Integer investTotal;

    private Integer totalRatio;

    private Long investAmountNum;

    private String investAmountStr;

    private Integer amountRatio;

    private String investProj;

    private List<String> industryIds;

    private List<String> investStages;

    private List<String> orgTypes;

    private List<Long> districtIds;

    private List<Long> districtSubIds;

    private List<String> capitalTypes;

    private List<String> currencys;

    private String order;

    private String orderBy;

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

    public String getFocusDomain() {
        return focusDomain;
    }

    public void setFocusDomain(String focusDomain) {
        this.focusDomain = focusDomain;
    }

    public String getInvestStage() {
        return investStage;
    }

    public void setInvestStage(String investStage) {
        this.investStage = investStage;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getCapitalType() {
        return capitalType;
    }

    public void setCapitalType(String capitalType) {
        this.capitalType = capitalType;
    }

    public String getCurrencyTitle() {
        return currencyTitle;
    }

    public void setCurrencyTitle(String currencyTitle) {
        this.currencyTitle = currencyTitle;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getInvestTotal() {
        return investTotal;
    }

    public void setInvestTotal(Integer investTotal) {
        this.investTotal = investTotal;
    }

    public Integer getTotalRatio() {
        return totalRatio;
    }

    public void setTotalRatio(Integer totalRatio) {
        this.totalRatio = totalRatio;
    }

    public Long getInvestAmountNum() {
        return investAmountNum;
    }

    public void setInvestAmountNum(Long investAmountNum) {
        this.investAmountNum = investAmountNum;
    }

    public String getInvestAmountStr() {
        return investAmountStr;
    }

    public void setInvestAmountStr(String investAmountStr) {
        this.investAmountStr = investAmountStr;
    }

    public Integer getAmountRatio() {
        return amountRatio;
    }

    public void setAmountRatio(Integer amountRatio) {
        this.amountRatio = amountRatio;
    }

    public String getInvestProj() {
        return investProj;
    }

    public void setInvestProj(String investProj) {
        this.investProj = investProj;
    }

    public List<String> getIndustryIds() {
        return industryIds;
    }

    public void setIndustryIds(List<String> industryIds) {
        this.industryIds = industryIds;
    }

    public List<String> getInvestStages() {
        return investStages;
    }

    public void setInvestStages(List<String> investStages) {
        this.investStages = investStages;
    }

    public List<String> getOrgTypes() {
        return orgTypes;
    }

    public void setOrgTypes(List<String> orgTypes) {
        this.orgTypes = orgTypes;
    }

    public List<String> getCapitalTypes() {
        return capitalTypes;
    }

    public void setCapitalTypes(List<String> capitalTypes) {
        this.capitalTypes = capitalTypes;
    }

    public List<String> getCurrencys() {
        return currencys;
    }

    public void setCurrencys(List<String> currencys) {
        this.currencys = currencys;
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
}
