package com.sdr.lib.util;

import android.content.Context;
import android.graphics.Color;

import com.sdr.lib.R;
import com.sdr.lib.SDR;
import com.sdr.lib.ui.toast.ToastTop;

/**
 * Created by HyFun on 2018/08/28.
 * Email:775183940@qq.com
 * 用于显示信息提示
 */

public class ToastTopUtil {
    private ToastTopUtil() {
    }

    private static ToastTop snackTopToast;

    public static void showCorrectTopToast(String title) {
        getInstance()
                .setBackgroundColor(Color.parseColor("#34C156"))
                .setTitle(title)
                .setIconRes(R.drawable.sdr_ic_success_outline_black_24dp)
                .setTitleColor(Color.WHITE)
                .setIconColor(Color.WHITE)
                .setShowTime(3000)
                .show();
    }

    public static void showErrorTopToast(String title) {
        getInstance()
                .setBackgroundColor(Color.parseColor("#FC443A"))
                .setTitle(title)
                .setIconRes(R.drawable.sdr_ic_warning_outline_black_24dp)
                .setTitleColor(Color.WHITE)
                .setIconColor(Color.WHITE)
                .setShowTime(3000)
                .show();
    }

    public static void showNormalTopToast(String title) {
        getInstance()
                .setBackgroundColor(Color.parseColor("#999999"))
                .setTitle(title)
                .setIconRes(R.drawable.sdr_ic_warning_outline_black_24dp)
                .setTitleColor(Color.WHITE)
                .setIconColor(Color.WHITE)
                .setShowTime(3000)
                .show();
    }


    //—————————————————————————私有方法———————————————————————————
    private static ToastTop getInstance() {
        if (snackTopToast == null) {
            synchronized (ToastTopUtil.class) {
                if (snackTopToast == null) {
                    snackTopToast = new ToastTop(getContext());
                }
            }
        }
        return snackTopToast;
    }

    private static Context getContext() {
        return SDR.getInstance().getApplication().getApplicationContext();
    }
}