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

    private String logoSmall;

    private String orgName;

    private Integer investTotal;

    private Integer totalRatio;

    private Long investAmountNum;

    private String investAmountStr;

    private Integer amountRatio;

    private String investProjJson;

    private List<String> industryIds;

    private List<Long> districtIds;

    private List<Long> districtSubIds;

    private List<String> investRounds;

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

    public String getLogoSmall() {
        return logoSmall;
    }

    public void setLogoSmall(String logoSmall) {
        this.logoSmall = logoSmall;
    }

    public String getInvestProjJson() {
        return investProjJson;
    }

    public void setInvestProjJson(String investProjJson) {
        this.investProjJson = investProjJson;
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

    public List<String> getInvestRounds() {
        return investRounds;
    }

    public void setInvestRounds(List<String> investRounds) {
        this.investRounds = investRounds;
    }
}
