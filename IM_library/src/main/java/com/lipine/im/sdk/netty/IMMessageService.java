package com.lipine.im.sdk.netty;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.lipine.im.sdk.event.CEventCenter;
import com.lipine.im.sdk.event.Events;
import com.lipine.im.sdk.event.I_CEventListener;
import com.lipine.im.sdk.listener.OnLoginCallback;
import com.lipine.im.sdk.protobuf.MessageProtobuf;

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
            Events.CHAT_SINGLE_MESSAGE
    };
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
                LogUtils.eTag(TAG,"收到了传递过来的消息"+obj);
                handlerLoginResult(obj);
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
        MessageProtobuf.Msg handshakeRespMsg = (MessageProtobuf.Msg) object;
        JSONObject jsonObj = JSON.parseObject(handshakeRespMsg.getHead().getExtend());
        int  status = jsonObj.getIntValue("status");
        if(status == 1){
            if (mOnLoginCallback != null) {
                mOnLoginCallback.onSuccess();
                mOnLoginCallback = null;
            }
        }else if(status == -1){
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
}
