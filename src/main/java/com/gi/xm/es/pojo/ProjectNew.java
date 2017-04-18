package com.gi.xm.es.pojo;

import java.io.Serializable;
import java.util.List;


/**
 * Created by zcy on 16-12-13
 * 项目 Elasticsearch 实体类
 * @author zhangchunyuan
 */
public class ProjectNew extends Pagination implements Serializable  {

    private Long sid;

    private String industryName;

    private String industrySubName;

    private String industryGrandSonName;

    private String industrySearch;

    private String newestEventRound;

    private String districtName;

    private String  pic;

    private String  title;

    private String  createDate;

    private String newestEventDate;

    private Long  newestEventMoney;

    private List<String> industryList;

    private List<String> roundList;

    private List<Long> districtIds;

    private List<Long> districtSubIds;

    private String startDate;

    private String endDate;

    private String order;

    private String orderBy;

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getIndustrySearch() {
        return industrySearch;
    }

    public void setIndustrySearch(String industrySearch) {
        this.industrySearch = industrySearch;
    }

    public void setNewestEventRound(String newestEventRound) {
        this.newestEventRound = newestEventRound;
    }

    public String getNewestEventRound() {
        return newestEventRound;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getNewestEventDate() {
        return newestEventDate;
    }

    public void setNewestEventDate(String newestEventDate) {
        this.newestEventDate = newestEventDate;
    }

    public Long getNewestEventMoney() {
        return newestEventMoney;
    }

    public void setNewestEventMoney(Long newestEventMoney) {
        this.newestEventMoney = newestEventMoney;
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

    public void setIndustryList(List<String> industryList) {
        this.industryList = industryList;
    }

    public List<String> getIndustryList() {
        return industryList;
    }
}
