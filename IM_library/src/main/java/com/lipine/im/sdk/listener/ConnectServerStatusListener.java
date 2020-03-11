package com.lipine.im.sdk.listener;


import com.lipine.im.sdk.LPStatusDefine;

/**
 * Time:2020/3/10
 * Author:lipine
 * Email:liqingsongandroid@163.com
 * Description: 连接服务器状态的监听
 */
public interface ConnectServerStatusListener {

    void onConnectionStatus(LPStatusDefine.LPConnectionStatus lpConnectionStatus);
}
