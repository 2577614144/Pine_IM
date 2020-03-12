package com.lipine.im;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lipine.im.base.BaseActivity;
import com.lipine.im.sdk.LPIMClient;
import com.lipine.im.sdk.LPStatusDefine;
import com.lipine.im.sdk.listener.OnLoginCallback;
import com.lipine.im.utils.ToastUtil;

public class MainActivity extends BaseActivity {
    private String serverUrl = "127.0.0.1";
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
                LPIMClient.initialize().connectWithAccount(serverUrl, port, domain, username, password, new OnLoginCallback() {
                    @Override
                    public void onAccountIncorrect() {
                        ToastUtil.show(mContext,"用户名密码错误",2000);
                    }

                    @Override
                    public void onSuccess() {
                        ToastUtil.show(mContext,"连接服务器成功",2000);
                    }

                    @Override
                    public void onError(LPStatusDefine.LPConnectErrorCode lpConnectErrorCode) {
                        ToastUtil.show(mContext,"用户名密码错误",2000);
                    }
                });
            }
        });
    }
}
