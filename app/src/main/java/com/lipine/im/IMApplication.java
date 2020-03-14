package com.lipine.im;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.lipine.im.sdk.LPIMClient;

/**
 * Time:2020/3/13
 * Author:lipine
 * Email:liqingsongandroid@163.com
 * Description:
 */
public class IMApplication extends MultiDexApplication {
    private Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = IMApplication.this;
        LPIMClient.getInstance().init(mContext);
    }
}
