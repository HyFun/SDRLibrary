package com.sdr.sdrlib.base;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

import com.sdr.sdrlib.R;

/**
 * Created by HyFun on 2018/11/23.
 * Email: 775183940@qq.com
 * Description:
 */

public class BaseConfig {
    public static final int onHeaderBarRes() {
        return R.layout.layout_public_toolbar_white;
    }

    public static final Drawable onHeaderBarDrawable(Context context) {
        ColorDrawable colorDrawable = new ColorDrawable(context.getResources().getColor(R.color.colorPrimary));
        return colorDrawable;
    }

    public static final int onHeaderBarStatusViewAlpha() {
        return 90;
    }

    public static final int onHeaderBarGravity() {
        return Gravity.CENTER;
    }

}
