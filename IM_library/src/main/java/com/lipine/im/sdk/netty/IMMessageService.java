package com.lipine.im.sdk.netty;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.lipine.im.sdk.LPIMClient;
import com.lipine.im.sdk.event.CEventCenter;
import com.lipine.im.sdk.event.Events;
import com.lipine.im.sdk.event.I_CEventListener;
import com.lipine.im.sdk.listener.OnLoginCallback;
import com.lipine.im.sdk.protobuf.MessageProtobuf;

import io.netty.channel.Channel;

/**
 * Time:2020/3/14
 * Author:lipine
 * Email:liqingsongandroid@163.com
 * Description: 所有消息接收和发送的服务
 */
public class IMMessageService  extends Service  implements I_CEventListener {

    private static final String TAG = "IMMessageService";
    public static OnLoginCallback mOnLoginCallback;//登录的回调接口

    public static void setOnLoginCallback(OnLoginCallback onLoginCallback) {
        mOnLoginCallback = onLoginCallback;
    }

    /**
     * 事件类型
     */
    private static final String[] EVENTS = {
            Events.CHAT_LOGIN_MESSAGE,
            Events.CHAT_HEARTBEAT_SEND_MESSAGE,
            Events.CHAT_HEARTBEAT_RECEIVED_MESSAGE,
            Events.CHAT_SINGLE_MESSAGE
    };

    private HeartbeatTask heartbeatTask;
    //通知栏显示的数字
    public  static int mMessageNumber = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        CEventCenter.registerEventListener(this, EVENTS);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onCEvent(String topic, int msgCode, int resultCode, Object obj) {
        switch (topic) {
            case Events.CHAT_LOGIN_MESSAGE: {
                LogUtils.eTag(TAG,"======登录的响应消息========"+obj);
                handlerLoginResult(obj);
                break;
            }
            /**
             *  客户端发送心跳
             */
            case Events.CHAT_HEARTBEAT_SEND_MESSAGE: {
                if (heartbeatTask == null) {
                    heartbeatTask = new HeartbeatTask(LPIMClient.getInstance().channel);
                }
                NettyTcpClient.getInstance().getLoopGroup().execWorkTask(heartbeatTask);
                break;
            }
            /**
             *  客户端接收心跳
             */
            case Events.CHAT_HEARTBEAT_RECEIVED_MESSAGE:{
                LogUtils.eTag(TAG,"===========接收到服务器响应心跳了====="+obj);
                break;
            }
            default:
                break;
        }
    }

    /**
     * 处理登录结果
     * @param object
     */
    private void handlerLoginResult(Object object) {
        MessageProtobuf.LogResponseMsg logResponseMsg = (MessageProtobuf.LogResponseMsg) object;
        int  status = logResponseMsg.getStatus();
        if(status == 1){
            //发送心跳
//            NettyTcpClient.getInstance().addHeartbeatHandler();
            if (mOnLoginCallback != null) {
                mOnLoginCallback.onSuccess(logResponseMsg);
                mOnLoginCallback = null;
            }
        }else if(status == 0){
            if (mOnLoginCallback != null) {
                mOnLoginCallback.onAccountIncorrect();
                mOnLoginCallback = null;
            }
        }
    }

    @Override
    public void onDestroy() {
        CEventCenter.unregisterEventListener(this, EVENTS);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class HeartbeatTask implements Runnable {

        private Channel ctx;

        public HeartbeatTask(Channel ctx) {
            this.ctx = ctx;
        }
        @Override
        public void run() {
            if (ctx.isActive()) {
                MessageProtobuf.Msg heartbeatMsg = NettyTcpClient.getInstance().getHeartbeatMsg();
                if (heartbeatMsg == null) {
                    return;
                }
                LogUtils.eTag(TAG,"======客户端发送心跳消息=========" + heartbeatMsg + "当前心跳间隔为：" + NettyTcpClient.getInstance().getHeartbeatInterval() + "ms\n");
                NettyTcpClient.getInstance().sendMsg(heartbeatMsg, false);
            }
        }
    }

}
