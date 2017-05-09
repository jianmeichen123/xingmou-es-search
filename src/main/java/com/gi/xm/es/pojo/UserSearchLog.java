//package com.gi.xm.es.pojo;
//
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import java.io.Serializable;
//
///**
// * Created by vincent on 16-12-19.
// */
//@Document(collection = "user_search_log")
//public class UserSearchLog implements Serializable {
//    @Id
//    private String id;
//    private String email;
//    private Integer roleId;
//    private String realName;
//    private String txt;
//    private Integer type;
//    private String returnjson;
//    private Long sendtime;
//
//    public String getEnv() {
//        return env;
//    }
//
//    public void setEnv(String env) {
//        this.env = env;
//    }
//
//    private String env;
//    public String getFrom() {
//        return from;
//    }
//
//    public void setFrom(String from) {
//        this.from = from;
//    }
//
//    private String from;
//
//    public String getRealName() {
//        return realName;
//    }
//
//    public void setRealName(String realName) {
//        this.realName = realName;
//    }
//
//    public Long getReturntime() {
//        return returntime;
//    }
//
//    public void setReturntime(Long returntime) {
//        this.returntime = returntime;
//    }
//
//    private Long returntime;
//
//    public UserSearchLog() {
//    }
//
//    public Long getSendtime() {
//        return sendtime;
//    }
//
//    public void setSendtime(Long sendtime) {
//        this.sendtime = sendtime;
//    }
//
//    public UserSearchLog(String id, String email, Integer roleId, String realName, String txt, Integer type, String returnjson, Integer pageNo, Long loadtime) {
//        this.id = id;
//        this.email = email;
//        this.roleId = roleId;
//        this.realName = realName;
//        this.txt = txt;
//        this.type = type;
//        this.returnjson = returnjson;
//        this.pageNo = pageNo;
//        this.loadtime = loadtime;
//    }
//
//    public Integer getPageNo() {
//        return pageNo;
//    }
//
//    public void setPageNo(Integer pageNo) {
//        this.pageNo = pageNo;
//    }
//
//    private Integer pageNo;
//
//    public Long getLoadtime() {
//        return loadtime;
//    }
//
//    public void setLoadtime(Long loadtime) {
//        this.loadtime = loadtime;
//    }
//
//    private Long loadtime;
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public Integer getRoleId() {
//        return roleId;
//    }
//
//    public void setRoleId(Integer roleId) {
//        this.roleId = roleId;
//    }
//
//    public String getTxt() {
//        return txt;
//    }
//
//    public void setTxt(String txt) {
//        this.txt = txt;
//    }
//
//    public Integer getType() {
//        return type;
//    }
//
//    public void setType(Integer type) {
//        this.type = type;
//    }
//
//    public String getReturnjson() {
//        return returnjson;
//    }
//
//    public void setReturnjson(String returnjson) {
//        this.returnjson = returnjson;
//    }
//}
