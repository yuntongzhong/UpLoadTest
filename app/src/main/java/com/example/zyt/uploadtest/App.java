package com.example.zyt.uploadtest;

import android.app.Application;

import com.tencent.bugly.Bugly;

/**
 * Created by Administrator on 2016/8/18.
 * Created by zyt on 2016/8/18.
 */
public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplicationContext(), "900047701", false);
    }
}
