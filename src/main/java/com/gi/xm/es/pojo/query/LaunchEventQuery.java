package com.gi.xm.es.pojo.query;

import com.gi.xm.es.pojo.Pagination;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zcy on 17-4-18.
 */
public class LaunchEventQuery extends Pagination implements Serializable {

    private Long id;

    private String code;

    private Long sourceId;

    private Long sourceCode;

    private String  industryName;

    private String  industrySubName;

    private String type;

    private String stockExchange;

    private String transferType;

    private String marketLayer;

    private String listedDate;

    private String district;

    private String logo;

    private String company;

    private String stockCode;

    private List<String> industryIds;

    private List<String> stockExchangeList;

    private List<String> transferTypeList;

    private List<String> marketLayerList;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(String stockExchange) {
        this.stockExchange = stockExchange;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getMarketLayer() {
        return marketLayer;
    }

    public void setMarketLayer(String marketLayer) {
        this.marketLayer = marketLayer;
    }

    public String getListedDate() {
        return listedDate;
    }

    public void setListedDate(String listedDate) {
        this.listedDate = listedDate;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public List<String> getIndustryIds() {
        return industryIds;
    }

    public void setIndustryIds(List<String> industryIds) {
        this.industryIds = industryIds;
    }

    public List<String> getStockExchangeList() {
        return stockExchangeList;
    }

    public void setStockExchangeList(List<String> stockExchangeList) {
        this.stockExchangeList = stockExchangeList;
    }

    public List<String> getTransferTypeList() {
        return transferTypeList;
    }

    public void setTransferTypeList(List<String> transferTypeList) {
        this.transferTypeList = transferTypeList;
    }

    public List<String> getMarketLayerList() {
        return marketLayerList;
    }

    public void setMarketLayerList(List<String> marketLayerList) {
        this.marketLayerList = marketLayerList;
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

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
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
