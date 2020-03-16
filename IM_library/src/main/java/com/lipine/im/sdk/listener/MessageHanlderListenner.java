package com.lipine.im.sdk.listener;


import com.lipine.im.sdk.bean.AppMessage;

/**
 * Time:2020/3/14
 * Author:lipine
 * Email:liqingsongandroid@163.com
 * Description: 所有消息处理的接口类
 */
public interface MessageHanlderListenner {

    void messageHander(AppMessage appMessage);
}
