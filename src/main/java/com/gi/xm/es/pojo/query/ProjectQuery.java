package com.gi.xm.es.pojo.query;

import com.gi.xm.es.pojo.Pagination;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zcy on 17-1-17.
 */
public class ProjectQuery extends Pagination implements Serializable{

    private String code;

    private Long sourceId;

    private String projTitle;

    private String industryName;

    private String industrySubName;

    private String industryGrandSonName;

    private String latestFinanceRound;

    private String addr;

    private String  logoSmall;

    private String  setupDT;

    private String latestFinanceDT;

    private String  latestFinanceAmountStr;

    private Long  latestFinanceAmountNum;

    private List<String> industryIds;

    private List<String> roundList;

    private List<Long> districtIds;

    private List<Long> districtSubIds;

    private String startDate;

    private String endDate;

    private String order;

    private String orderBy;

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
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

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getLogoSmall() {
        return logoSmall;
    }

    public void setLogoSmall(String logoSmall) {
        this.logoSmall = logoSmall;
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

    public Long getLatestFinanceAmountNum() {
        return latestFinanceAmountNum;
    }

    public void setLatestFinanceAmountNum(Long latestFinanceAmountNum) {
        this.latestFinanceAmountNum = latestFinanceAmountNum;
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


    public void setRoundList(List<String> roundList) {
        this.roundList = roundList;
    }

    public List<String> getRoundList() {
        return roundList;
    }

    public void setIndustryGrandSonName(String industryGrandSonName) {
        this.industryGrandSonName = industryGrandSonName;
    }

    public String getIndustryGrandSonName() {
        return industryGrandSonName;
    }

    public void setIndustryIds(List<String> industryIds) {
        this.industryIds = industryIds;
    }

    public List<String> getIndustryIds() {
        return industryIds;
    }
}
