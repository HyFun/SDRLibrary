package com.sdr.lib.util;

import android.content.Context;
import android.graphics.Color;

import com.sdr.lib.SDRLibrary;
import com.sdr.lib.ui.toast.ToastSimple;

/**
 * Created by  HYF on 2018/7/10.
 * Email：775183940@qq.com
 */

public class ToastUtil {
    private static ToastSimple niceToast;

    private ToastUtil() {
    }

    private static final ToastSimple getInstance() {
        if (niceToast == null)
            synchronized (ToastUtil.class) {
                if (niceToast == null)
                    niceToast = new ToastSimple(getContext()).radius(CommonUtil.dip2px(getContext(), 5));
            }
        return niceToast;
    }

    public static final void showCorrectMsg(String msg) {
        getInstance()
                .background(Color.parseColor("#34C156"))
                .text(msg)
                .show();
    }

    public static final void showNormalMsg(String msg) {
        getInstance()
                .background(Color.parseColor("#999999"))
                .text(msg)
                .show();
    }

    public static final void showErrorMsg(String msg) {
        getInstance()
                .background(Color.parseColor("#FC443A"))
                .text(msg)
                .show();
    }

    private static Context getContext() {
        return SDRLibrary.getInstance().getApplication().getApplicationContext();
    }
}
