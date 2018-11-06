package com.sdr.lib;

import android.app.Application;
import android.support.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Created by HYF on 2018/10/13.
 * Email：775183940@qq.com
 */

public class SDRLibrary {

    private SDRLibrary() {
    }

    private static SDRLibrary instance;

    public static SDRLibrary getInstance() {
        if (instance == null) {
            synchronized (SDRLibrary.class) {
                if (instance == null) {
                    instance = new SDRLibrary();
                }
            }
        }
        return instance;
    }


    private Application application;
    private boolean debug;

    public void init(Application application, final boolean debug) {
        this.application = application;
        this.debug = debug;
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
                return debug;
            }
        });
    }

    public Application getApplication() {
        return application;
    }

    public boolean isDebug() {
        return debug;
    }
}
