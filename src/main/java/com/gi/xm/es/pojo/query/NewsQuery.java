package com.gi.xm.es.pojo.query;

import com.gi.xm.es.pojo.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by zcy on 17-4-18.
 */
@ApiModel
public class NewsQuery extends Query implements Serializable {


    private Long id;

    @ApiModelProperty("作者")
    private String auther;

    @ApiModelProperty("行业")
    private String industry;

    @ApiModelProperty("新闻图片地址:'http://static.galaxyinternet.com/img/news/'+imgmd5+'.PNG'")
    private String code;

    @ApiModelProperty("创建时间")
    private Long ctime;

    @ApiModelProperty("点击链接")
    private String href;

    @ApiModelProperty("时间排序字段 默认按时间倒序 不用传值")
    private Long orderTime;

    @ApiModelProperty("暂无用")
    private String searchkey;

    @ApiModelProperty("资讯概要")
    private String overview;

    @ApiModelProperty("资讯类型")
    private String type;

    @ApiModelProperty("资讯类型 [0:项目 1:机构 2:大公司3:事件4:任务5:政策6:行业 7:新产品")
    private Integer typeId;

    @ApiModelProperty("暂无用")
    private String md5;

    @ApiModelProperty("资讯标题")
    private String title;

    @ApiModelProperty("asc/desc")
    private String order;

    @ApiModelProperty("排序字段")
    private String orderBy;

    @ApiModelProperty("资讯图片")
    private String imgmd5;

    private String industryNames;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getSearchkey() {
        return searchkey;
    }

    public void setSearchkey(String searchkey) {
        this.searchkey = searchkey;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Long orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

	public String getIndustryNames() {
		return industryNames;
	}

	public void setIndustryNames(String industryNames) {
		this.industryNames = industryNames;
	}

    public String getImgmd5() {
        return imgmd5;
    }

    public void setImgmd5(String imgmd5) {
        this.imgmd5 = imgmd5;
    }
}
