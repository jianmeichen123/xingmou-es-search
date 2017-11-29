package com.gi.xm.es.pojo.query;

import com.gi.xm.es.pojo.Pagination;
import com.gi.xm.es.pojo.Query;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zcy on 17-4-24.
 */
public class InvestFirmsQuery extends Query implements Serializable {

    private Long id;

    private String orgCode;

    private String investOrg;

    private Integer investTotal;

    private Integer totalRatio;

    private Long investAmountNum;

    private String investAmountStr;

    private Integer amountRatio;

    private String orgProjJson;

    private String investStage;

    private String orgType;

    private String orgDesc;

    private Long districtId;

    private Long districtSubId;

    private List<String> industryIds;

    private List<Long> districtIds;

    private List<Long> districtSubIds;

    private List<String> orgRounds;

    private String order;

    private String orderBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgProjJson() {
        return orgProjJson;
    }

    public void setOrgProjJson(String orgProjJson) {
        this.orgProjJson = orgProjJson;
    }

    public String getInvestOrg() {
        return investOrg;
    }

    public void setInvestOrg(String investOrg) {
        this.investOrg = investOrg;
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

    public List<String> getIndustryIds() {
        return industryIds;
    }

    public void setIndustryIds(List<String> industryIds) {
        this.industryIds = industryIds;
    }

    public String getInvestStage() {
        return investStage;
    }

    public void setInvestStage(String investStage) {
        this.investStage = investStage;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getDistrictSubId() {
        return districtSubId;
    }

    public void setDistrictSubId(Long districtSubId) {
        this.districtSubId = districtSubId;
    }

    public List<String> getOrgRounds() {
        return orgRounds;
    }

    public void setOrgRounds(List<String> orgRounds) {
        this.orgRounds = orgRounds;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgDesc() {
        return orgDesc;
    }

    public void setOrgDesc(String orgDesc) {
        this.orgDesc = orgDesc;
    }
}
