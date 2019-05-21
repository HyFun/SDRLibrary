package com.sdr.lib.util;

import android.content.Context;

import com.sdr.lib.R;
import com.sdr.lib.SDR_LIBRARY;
import com.sdr.lib.ui.toast.ToastSimple;

/**
 * Created by  HYF on 2018/7/10.
 * Email：775183940@qq.com
 */
@Deprecated
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

    public static final void showPositiveToast(String msg) {
        getInstance()
                .background(getContext().getResources().getColor(R.color.colorPositive))
                .text(msg)
                .show();
    }

    public static final void showNegativeToast(String msg) {
        getInstance()
                .background(getContext().getResources().getColor(R.color.colorNegative))
                .text(msg)
                .show();
    }

    public static final void showNormalToast(String msg) {
        getInstance()
                .background(getContext().getResources().getColor(R.color.colorPrimary))
                .text(msg)
                .show();
    }


    private static Context getContext() {
        return SDR_LIBRARY.getInstance().getApplication().getApplicationContext();
    }
}
