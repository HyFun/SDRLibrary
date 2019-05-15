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
@Deprecated
public class ToastTopUtil {
    private ToastTopUtil() {
    }

    private static ToastTop snackTopToast;

    public static void showPositiveTopToast(String title) {
        getInstance()
                .setBackgroundColor(Color.WHITE)
                .setTitle(title)
                .setIconRes(R.drawable.sdr_ic_toast_success_24dp)
                .setTitleColor(getContext().getResources().getColor(R.color.colorBlack))
                .setIconColor(getContext().getResources().getColor(R.color.colorPositive))
                .setShowTime(3000)
                .show();
    }

    public static void showNegativeTopToast(String title) {
        getInstance()
                .setBackgroundColor(Color.WHITE)
                .setTitle(title)
                .setIconRes(R.drawable.sdr_ic_toast_warn_24dp)
                .setTitleColor(getContext().getResources().getColor(R.color.colorBlack))
                .setIconColor(getContext().getResources().getColor(R.color.colorNegative))
                .setShowTime(3000)
                .show();
    }

    public static void showNormalTopToast(String title) {
        getInstance()
                .setBackgroundColor(Color.WHITE)
                .setTitle(title)
                .setIconRes(R.drawable.sdr_ic_toast_warn_24dp)
                .setTitleColor(getContext().getResources().getColor(R.color.colorBlack))
                .setIconColor(getContext().getResources().getColor(R.color.colorPrimary))
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