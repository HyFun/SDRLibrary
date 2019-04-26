package com.sdr.lib.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.view.Gravity;

/**
 * Created by HyFun on 2019/04/26.
 * Email: 775183940@qq.com
 * Description: 配置全局baseactivity
 */

public abstract class BaseActivityConfig {

    /**
     * 配置状态栏透明度
     *
     * @return
     */
    @IntRange(from = 0, to = 255)
    public int onHeaderBarStatusViewAlpha() {
        return 0;
    }

    /**
     * header bar 标题文字的位置
     * 默认显示在左侧
     *
     * @return
     */
    public int onHeaderBarTitleGravity() {
        return Gravity.LEFT;
    }

    /**
     * 如果使用 headerbar 设置toolbar的资源文件
     * <p>
     * 如果不使用  return 0 即可
     *
     * @return
     */
    public abstract int onHeaderBarToolbarRes();

    /**
     * 设置HeaderbarView的颜色 drawable
     *
     * @return
     */
    public abstract Drawable onHeaderBarDrawable();

}
