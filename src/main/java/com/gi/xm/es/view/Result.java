package com.gi.xm.es.view;

import com.gi.xm.es.pojo.Pagination;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zcy on 16-11-24.
 */
public class Result implements Serializable {

    /*
     *  返回信息
     */
    private String msg;

    /**
     * 返回状态码
     */
    private int status;

    /**
     * 分页数据
     */
    private Pagination data;

    private Map<String,Long> numMap;

    public Result(String msg, int status) {
        this.msg = msg;
        this.status = status;
    }

    public Result(String msg, int status, Pagination data) {
        this.msg = msg;
        this.status = status;
        this.data = data;
    }

    public Result() {
        this.msg = MessageStatus.OK.getMessage();
        this.status = MessageStatus.OK.getStatus();
    }

    public static Result addOK() {
        Result ret = new Result(MessageStatus.OK.getMessage(), MessageStatus.OK.getStatus());
        return ret;
    }

    public static Result addError() {
        Result ret = new Result(MessageStatus.SYS_ERROR.getMessage(), MessageStatus.SYS_ERROR.getStatus());
        return ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Pagination getData() {
        return data;
    }

    public void setData(Pagination page) {
        this.data = data;
    }

    public Map<String, Long> getNumMap() {
        return numMap;
    }

    public void setNumMap(Map<String, Long> numMap) {
        this.numMap = numMap;
    }
}
