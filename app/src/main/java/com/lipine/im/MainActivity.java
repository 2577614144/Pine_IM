package com.lipine.im;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.lipine.im.base.BaseActivity;
import com.lipine.im.sdk.LPIMClient;
import com.lipine.im.sdk.LPStatusDefine;
import com.lipine.im.sdk.listener.OnLoginCallback;

public class MainActivity extends BaseActivity {
//    private String serverUrl = "10.23.6.31";
//    private String serverUrl = "127.0.0.1";
    private String serverUrl = "192.168.137.1";
    private int port = 8855;
    private String domain = "itjavaweb.com";
    private String username = "111";
    private String password = "222";
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
                    public void onSuccess() {
                        ToastUtils.showShort("连接服务器成功");
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
