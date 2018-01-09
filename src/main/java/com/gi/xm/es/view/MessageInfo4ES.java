package com.gi.xm.es.view;

import com.gi.xm.es.view.Pagination;
import io.swagger.annotations.ApiModelProperty;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zcy on 17-12-4.
 */
public class MessageInfo4ES{
    /**
     * @Fields serialVersionUID : TODO
     */
    private static final long serialVersionUID = -7122226153545621086L;
    @ApiModelProperty(value = "OK/ERROR/缺少参数")
    private String message;

    @ApiModelProperty(value = "成功:10000 失败:10001 缺少参数:10002")
    private int status;
    /**
     *
     */
    @ApiModelProperty(value="分组统计数据")
    private List<AppNews> data;
    /**
     * 返回分页数据
     */
    @ApiModelProperty(value = "分页数据")
    Pagination page;

    /**
     * ES每个索引返回的命中条数加和
     */
    @ApiModelProperty(value = "ES每个索引返回的命中条数加和")
    private Long totalhit;

    /**
     * ES每个索引返回的命中条数
     */
    @ApiModelProperty(value = "ES每个索引返回的命中条数")
    private LinkedHashMap<String,Long> numMap;

    @ApiModelProperty(value = "true/false")
    private boolean success;

    @ApiModelProperty(value = "ES每个索引返回的命中条数")
    private LinkedHashMap<String,String> resultMap;

    public MessageInfo4ES()
    {
        this.setStatus(MessageStatus.OK.getStatus());
        this.setMessage("成功");
    }

    public MessageInfo4ES(int status2, String message2) {
        this.setStatus(status2);
        this.setMessage(message2);
    }

    public MessageInfo4ES(int status2, String message2,Pagination page) {
        this.setStatus(status2);
        this.setMessage(message2);
        this.setPage(page);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;

    }

    public void setPage(Pagination page) {
        this.page = page;
    }

    public Pagination getPage() {
        return page;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return status == MessageStatus.OK.getStatus();
    }

    public Long getTotalhit() {
        return totalhit;
    }

    public void setTotalhit(Long totalhit) {
        this.totalhit = totalhit;
    }

    public LinkedHashMap<String, Long> getNumMap() {
        return numMap;
    }

    public void setNumMap(LinkedHashMap<String, Long> numMap) {
        this.numMap = numMap;
    }

    public LinkedHashMap<String, String> getResultMap() {
        return resultMap;
    }

    public void setResultMap(LinkedHashMap<String, String> resultMap) {
        this.resultMap = resultMap;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<AppNews> getData() {
        return data;
    }

    public void setData(List<AppNews> data) {
        this.data = data;
    }
}
