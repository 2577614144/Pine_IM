package com.lipine.im.sdk;

import android.text.TextUtils;

import com.lipine.im.sdk.listener.ConnectServerStatusListener;
import com.lipine.im.sdk.listener.OnLoginCallback;
import com.lipine.im.sdk.netty.NettyTcpClient;

/**
 * Time:2020/3/9
 * Author:lipine
 * Email:liqingsongandroid@163.com
 * Description:
 */
public class LPIMClient {

    private LPIMClient (){}

    private volatile LPIMClient client = null;

    public LPIMClient initialize(){
        if(client == null){
            synchronized (LPIMClient.class) {
                if (client == null) {
                    client = new LPIMClient();
                }
            }
        }
        return client;
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

    private void connectServerAndLogin(String userName, String passWord, OnLoginCallback onLoginCallback) {
        if(TextUtils.isEmpty(userName)){
            onLoginCallback.onError(LPStatusDefine.LPConnectErrorCode.LPConnectErrorCode_type1);
            return;
        }
        if(TextUtils.isEmpty(passWord)){
            onLoginCallback.onError(LPStatusDefine.LPConnectErrorCode.LPConnectErrorCode_type1);
            return;
        }
        NettyTcpClient.getInstance().init(LPIMConfig.SERVER_IP, LPIMConfig.SERVER_PORT, new ConnectServerStatusListener() {
            @Override
            public void onConnectionStatus(LPStatusDefine.LPConnectionStatus lpConnectionStatus) {

            }
        });
    }


}
