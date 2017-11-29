package com.gi.xm.es.pojo.query;

import com.gi.xm.es.pojo.Pagination;
import com.gi.xm.es.pojo.Query;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zcy on 17-4-18.
 */
public class LaunchEventQuery extends Query implements Serializable {

    private Long eventId;

    private String code;

    private Long sourceId;

    private String sourceCode;

    private String  industryName;

    private String  industrySubName;

    private String type;

    private String stockExchange;

    private String transferType;

    private String marketLayer;

    private String listedDate;

    private String districtSubName;

    private String projTitle;

    private String stockCode;

    private List<String> industryIds;

    private List<String> stockExchanges;

    private List<String> types;

    private List<String> marketLayers;

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

    public List<String> getStockExchanges() {
        return stockExchanges;
    }

    public void setStockExchanges(List<String> stockExchanges) {
        this.stockExchanges = stockExchanges;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getMarketLayers() {
        return marketLayers;
    }

    public void setMarketLayers(List<String> marketLayers) {
        this.marketLayers = marketLayers;
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

    public String getDistrictSubName() {
        return districtSubName;
    }

    public void setDistrictSubName(String districtSubName) {
        this.districtSubName = districtSubName;
    }

    public String getProjTitle() {
        return projTitle;
    }

    public void setProjTitle(String projTitle) {
        this.projTitle = projTitle;
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
}
