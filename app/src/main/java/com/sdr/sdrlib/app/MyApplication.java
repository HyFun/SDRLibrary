package com.sdr.sdrlib.app;

import android.app.Application;

import com.sdr.lib.BuildConfig;
import com.sdr.lib.SDRLibrary;

/**
 * Created by HyFun on 2018/11/06.
 * Email:775183940@qq.com
 */

public class MyApplication extends Application {
    private static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        SDRLibrary.getInstance().init(application, BuildConfig.DEBUG);
    }


    public static MyApplication getApplication() {
        return application;
    }
}
