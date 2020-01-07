package com.sdr.lib;

import android.app.Application;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
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

public class SDR_LIBRARY {

    private SDR_LIBRARY() {
    }

    private static SDR_LIBRARY instance;

    public static SDR_LIBRARY getInstance() {
        if (instance == null) {
            synchronized (SDR_LIBRARY.class) {
                if (instance == null) {
                    instance = new SDR_LIBRARY();
                }
            }
        }
        return instance;
    }


    private Application application;
    private boolean debug;
    private BaseActivityConfig activityConfig;
    private Glide glide;


    /**
     * 注册
     * 需要在 application中注册 并做一些初始化操作
     *
     * @param application
     */
    public static void register(Application application, BaseActivityConfig activityConfig) {
        getInstance().setApplication(application);
        getInstance().setDebug(CommonUtil.isApkInDebug(application));
        getInstance().setActivityConfig(activityConfig);

        // 初始化logger
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
        Logger.addLogAdapter(new DiskLogAdapter(CsvFormatStrategy.newBuilder().path(application.getExternalCacheDir().getAbsolutePath()).build()));
        // RxActivityResult初始化操作
        RxActivityResult.register(application);
    }


    public Application getApplication() {
        return application;
    }

    private void setApplication(Application application) {
        this.application = application;
    }

    public boolean isDebug() {
        return debug;
    }

    private void setDebug(boolean debug) {
        this.debug = debug;
    }

    public BaseActivityConfig getActivityConfig() {
        return activityConfig;
    }

    private void setActivityConfig(BaseActivityConfig activityConfig) {
        this.activityConfig = activityConfig;
    }

    public Glide getGlide() {
        if (glide == null) {
            glide = Glide.get(application);
        }
        return glide;
    }

    public void setGlide(Glide glide) {
        this.glide = glide;
    }
}
