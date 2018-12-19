package com.sdr.sdrlib.util;

import android.Manifest;
import android.support.v4.app.FragmentActivity;

import com.sdr.lib.support.path.AppPath;
import com.sdr.lib.support.update.AppNeedUpdateListener;
import com.sdr.lib.support.update.UpdateAppManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * Created by HyFun on 2018/11/06.
 * Email:775183940@qq.com
 */

public class AppUtil {
    private AppUtil() {
    }

    /**
     * 检测app更新
     *
     * @param activity
     * @param showDialog
     * @param needUpdateListener
     */
    public static void checkUpdate(FragmentActivity activity, boolean showDialog, AppNeedUpdateListener needUpdateListener) {
        //if (BuildConfig.DEBUG) return;
        new RxPermissions(activity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        UpdateAppManager.checkUpdate(activity, "bpm", 1, AppPath.getFilePath(), showDialog, needUpdateListener);
                    } else {

                    }
                });
    }
}
