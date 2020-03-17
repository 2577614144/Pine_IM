package com.lipine.im.sdk;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.lipine.im.sdk.bean.MessageType;
import com.lipine.im.sdk.listener.ConnectServerStatusListener;
import com.lipine.im.sdk.listener.OnLoginCallback;
import com.lipine.im.sdk.netty.IMMessageService;
import com.lipine.im.sdk.netty.NettyTcpClient;
import com.lipine.im.sdk.processor.MessageProcessor;
import com.lipine.im.sdk.protobuf.MessageProtobuf;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * Time:2020/3/9
 * Author:lipine
 * Email:liqingsongandroid@163.com
 * Description: SDK相关类
 */
public class LPIMClient {
    private static final String TAG = "LPIMClient";

    private LPIMClient (){}

    private static volatile LPIMClient client = null;

    public static LPIMClient getInstance(){
        if(client == null){
            synchronized (LPIMClient.class) {
                if (client == null) {
                    client = new LPIMClient();
                }
            }
        }
        return client;
    }

    public Channel channel;

    public Context mContext;

    public void init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context异常");
        } else {
            this.mContext = context;
            LogUtils.getConfig().setLogSwitch(true).setConsoleSwitch(true);
            CrashUtils.init(new CrashUtils.OnCrashListener() {
                @Override
                public void onCrash(String crashInfo, Throwable e) {
                    LogUtils.eTag(TAG,"崩溃的异常信息为："+crashInfo);
                }
            });

            //启动守护服务
            mContext.startService(new Intent(mContext, IMMessageService.class));
        }
    }

    /**
     * 自定义服务器地址 连接登录方法 通过用户密码 连接服务器 并登陆
     *
     * @param serviceIp         ip
     * @param servicePort       端口
     * @param serviceDomain     域名
     * @param userName          账号
     * @param passWord          密码
     * @param onLoginCallback
     */
    public void connectWithAccount(String serviceIp, int servicePort, String serviceDomain, String userName, String passWord, OnLoginCallback onLoginCallback) {
        LPIMConfig.SERVER_IP = serviceIp;
        LPIMConfig.SERVER_PORT = servicePort;
        LPIMConfig.SERVER_DOMAIN = serviceDomain;
        this.connectServerAndLogin(userName, passWord, onLoginCallback);
    }

    /**
     * 连接服务器并登陆
     * @param userName   用户名
     * @param passWord   密码
     * @param onLoginCallback 登陆的回调
     */
    private void connectServerAndLogin(final String userName, final String passWord, final OnLoginCallback onLoginCallback) {
        if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(passWord)){
            if(onLoginCallback!= null){
                onLoginCallback.onError(LPStatusDefine.LPConnectErrorCode.LPConnectErrorCode_type1);
            }
            return;
        }
        /**
         * 连接服务的状态
         */
        NettyTcpClient.getInstance().init(LPIMConfig.SERVER_IP, LPIMConfig.SERVER_PORT, new ConnectServerStatusListener() {
            @Override
            public void onConnectionStatus(LPStatusDefine.LPConnectionStatus lpConnectionStatus) {
                //如果连接服务器成功
                if(lpConnectionStatus == LPStatusDefine.LPConnectionStatus.LPCONNECTIONSTATUS_CONNECT_SUCCEED){
                    try {
                        IMMessageService.setOnLoginCallback(onLoginCallback);
                        login(LPIMConfig.SERVER_IP, LPIMConfig.SERVER_PORT,userName,passWord);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else{
                    if(onLoginCallback!= null){
                        onLoginCallback.onError(LPStatusDefine.LPConnectErrorCode.LPConnectErrorCode_type6);
                    }
                }
            }
        });
    }

    /**
     * 登录方法
     * 发送登录协议
     * @param serviceIp 服务器地址
     * @param userName  用户名
     * @param passWord  密码
     * @throws UnsupportedEncodingException
     */
    public void login(String serviceIp, int port,String userName, String passWord) throws UnsupportedEncodingException {
        if(channel.isActive() && channel.isOpen()){
            MessageProtobuf.Msg msg =  MessageProcessor.getHandshakeAndLoginMsg(userName,passWord,LPIMConfig.SERVER_IP, LPIMConfig.SERVER_PORT);
            channel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        // 发送成功，等待服务器响应
                        LogUtils.eTag(TAG, String.format("发送消息(ip[%s], port[%s])成功",LPIMConfig.SERVER_IP, LPIMConfig.SERVER_PORT));
                    } else {
                        // 发送失败
                        LogUtils.eTag(TAG, String.format("发送消息(ip[%s], port[%s])失败", LPIMConfig.SERVER_IP, LPIMConfig.SERVER_PORT));
                    }
                }
            });
        }
    }




}
