package com.lipine.im.sdk.listener;

import com.lipine.im.sdk.LPStatusDefine;
import com.lipine.im.sdk.protobuf.MessageProtobuf;

/**
 * Time:2020/3/9
 * Author:lipine
 * Email:liqingsongandroid@163.com
 * Description: 连接是否成功的监听
 */
public interface OnLoginCallback {

    //用户名密码错误
    void onAccountIncorrect();
    //登录成功
    void onSuccess(MessageProtobuf.LogResponseMsg logResponseMsg);

    void onError(LPStatusDefine.LPConnectErrorCode lpConnectErrorCode);
}
