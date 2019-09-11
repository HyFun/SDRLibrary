package com.sdr.sdrlib.app;

import android.app.Application;

import com.sdr.lib.SDR_LIBRARY;
import com.sdr.sdrlib.GlideApp;
import com.sdr.sdrlib.base.ActivityConfig;

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
        SDR_LIBRARY.register(application, new ActivityConfig(application));
        SDR_LIBRARY.getInstance().setGlide(GlideApp.get(application));
    }


    public static MyApplication getApplication() {
        return application;
    }
}
