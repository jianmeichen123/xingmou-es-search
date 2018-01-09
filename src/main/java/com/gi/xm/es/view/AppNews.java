package com.gi.xm.es.view;

import com.gi.xm.es.pojo.query.NewsQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by zcy on 18-1-9.
 */
@ApiModel
public class AppNews {
    @ApiModelProperty("资讯类型")
    private Long typeId;

    @ApiModelProperty("类型下查询数量")
    private Long number;

    @ApiModelProperty("类型下查询前n条")
    private List newsList;

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public List getNewsList() {
        return newsList;
    }

    public void setNewsList(List newsList) {
        this.newsList = newsList;
    }
}
