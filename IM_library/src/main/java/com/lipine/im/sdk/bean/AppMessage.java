package com.lipine.im.sdk.bean;
/**
 * Time:  2020/3/13 17:45
 * Author: lipine
 * Email: liqingsongandroid@163.com
 * Description: 用于把protobuf消息转换成app可用的消息类型
 */

public class AppMessage {

    private Head head;  // 消息头
    private String body;// 消息体

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    @Override
    public String toString() {
        return "AppMessage{" +
                "head=" + head +
                ", body='" + body + '\'' +
                '}';
    }
}
