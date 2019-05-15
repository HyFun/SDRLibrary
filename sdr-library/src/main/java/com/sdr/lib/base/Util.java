package com.sdr.lib.base;

import android.content.Context;

/**
 * Created by HyFun on 2018/09/20.
 * Email:775183940@qq.com
 */

class Util {
    /**
     * 将dp转换成系统使用的px
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return 转换后的px值
     */
    public final static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
