package com.lipine.im;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.lipine.im.base.BaseActivity;
import com.lipine.im.sdk.LPIMClient;
import com.lipine.im.sdk.LPStatusDefine;
import com.lipine.im.sdk.listener.OnLoginCallback;
import com.lipine.im.sdk.protobuf.MessageProtobuf;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
//    private String serverUrl = "10.23.6.31";
//    private String serverUrl = "127.0.0.1";
    private String serverUrl = "192.168.137.1";
    private int port = 8899;
    private String domain = "itjavaweb.com";
    private String username = "lipine";
    private String password = "123456";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.connect_server).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LPIMClient.getInstance().connectWithAccount(serverUrl, port, domain, username, password, new OnLoginCallback() {
                    @Override
                    public void onAccountIncorrect() {
                        ToastUtils.showShort("用户名密码错误");
                    }

                    @Override
                    public void onSuccess(MessageProtobuf.LogResponseMsg logResponseMsg) {
                        ToastUtils.showShort("用户名密码正确，跳转首页");
                    }


                    @Override
                    public void onError(LPStatusDefine.LPConnectErrorCode lpConnectErrorCode) {
                        ToastUtils.showShort("用户名密码错误");
                    }
                });
            }
        });
    }
}
