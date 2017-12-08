package com.gi.xm.es.view;

/**
 * Created by zcy on 16-11-24.
 */
public enum MessageStatus {

    OK(10000, "OK"),
    SYS_ERROR(10001, "ERROR"),
    MISS_PARAMETER(10002, "%s"),
    DATA_NOT_EXISTS(10003, "%s");

    private int status;
    private String message;

    MessageStatus(
            int status, String message) {
        this.setStatus(status);
        this.setMessage(message);
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

    public void setStatus(int status) {
        this.status = status;
    }
}
