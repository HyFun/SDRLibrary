package com.sdr.lib.util;

import android.app.Activity;
import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sdr.lib.R;
import com.sdr.lib.SDR_LIBRARY;
import com.sdr.lib.ui.toast.ToastTop;

/**
 * Created by HyFun on 2019/04/28.
 * Email: 775183940@qq.com
 * Description: 负责简单的弹框的工具类
 */

public class AlertUtil {
    private AlertUtil() {
    }

    // ————————————————————————————————————————————————————
    /**
     * ——————————————————————Toast Top——————————————————————
     */
    private static ToastTop toastTop;

    private static ToastTop getToastTop() {
        if (toastTop == null) {
            synchronized (AlertUtil.class) {
                if (toastTop == null) {
                    toastTop = new ToastTop(getContext());
                }
            }
        }
        return toastTop;
    }

    public static void showPositiveToastTop(String title, String content) {
        getToastTop()
                .setIconRes(R.drawable.sdr_ic_toast_success_24dp)
                .setIconColor(getContext().getResources().getColor(R.color.colorToastSuccess))
                .setTitle(title)
                .setContent(content)
                .setShowTime(3000)
                .show();
    }

    public static void showNormalToastTop(String title, String content) {
        getToastTop()
                .setIconRes(R.drawable.sdr_ic_toast_warn_24dp)
                .setIconColor(getContext().getResources().getColor(R.color.colorToastInfo))
                .setTitle(title)
                .setContent(content)
                .setShowTime(3000)
                .show();
    }

    public static void showNegativeToastTop(String title, String content) {
        getToastTop()
                .setIconRes(R.drawable.sdr_ic_toast_warn_24dp)
                .setIconColor(getContext().getResources().getColor(R.color.colorToastWarning))
                .setTitle(title)
                .setContent(content)
                .setShowTime(3000)
                .show();
    }


    /**
     * ——————————————————————Dialog——————————————————————
     */
    public static void showDialog(Activity activity, String title, String conten) {
        showDialog(activity, title, conten, null, null);
    }

    public static void showDialog(Activity activity, String title, String conten, MaterialDialog.SingleButtonCallback positiveListener) {
        showDialog(activity, title, conten, positiveListener, null);
    }

    public static void showDialog(Activity activity, String title, String content, MaterialDialog.SingleButtonCallback positiveListener, MaterialDialog.SingleButtonCallback negativeListener) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity)
                .title(title)
                .content(content)
                .positiveText("确定");
        if (positiveListener != null) {
            builder
                    .cancelable(false)
                    .onPositive(positiveListener);
        }
        if (negativeListener != null) {
            builder.negativeText("取消")
                    .onNegative(negativeListener);
        }
        builder.build()
                .show();
    }


    // ———————————————————————私有方法—————————————————————————
    private static Context getContext() {
        return SDR_LIBRARY.getInstance().getApplication().getApplicationContext();
    }
}
