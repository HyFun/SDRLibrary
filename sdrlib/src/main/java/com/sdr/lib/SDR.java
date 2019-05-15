package com.sdr.lib;

import android.app.Application;
import android.support.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.sdr.lib.base.BaseActivityConfig;
import com.sdr.lib.util.CommonUtil;

import rx_activity_result2.RxActivityResult;

/**
 * Created by HYF on 2018/10/13.
 * Email：775183940@qq.com
 */

public class SDR {

    private SDR() {
    }

    private static SDR instance;

    public static SDR getInstance() {
        if (instance == null) {
            synchronized (SDR.class) {
                if (instance == null) {
                    instance = new SDR();
                }
            }
        }
        return instance;
    }


    private Application application;
    private boolean debug;
    private BaseActivityConfig activityConfig;


    /**
     * 注册
     * 需要在 application中注册
     *
     * @param application
     */
    public static void register(Application application, BaseActivityConfig activityConfig) {
        getInstance().setApplication(application);
        getInstance().setDebug(CommonUtil.isApkInDebug(application));
        getInstance().setActivityConfig(activityConfig);


        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//                .methodCount(0)         // (Optional) How many method line to show. Default 2
//                .methodOffset(2)        // (Optional) Hides internal method calls up to offset. Default 5
                //.logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("Logger日志")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return getInstance().isDebug();
            }
        });

        RxActivityResult.register(application);
    }


    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public BaseActivityConfig getActivityConfig() {
        return activityConfig;
    }

    public void setActivityConfig(BaseActivityConfig activityConfig) {
        this.activityConfig = activityConfig;
    }
}
