package com.lipine.im.sdk.bean;

/**
 * Time:2020/3/14
 * Author:lipine
 * Email:liqingsongandroid@163.com
 * Description:
 */
public class LoginResult {
    //用户名
    private String name;
    //用户id
    private String userId;
    //票据
    private String ticket;
    //错误信息
    private String errorMessage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", ticket='" + ticket + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
