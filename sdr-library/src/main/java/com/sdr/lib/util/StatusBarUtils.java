package com.sdr.lib.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.ContentFrameLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by zhouwei on 16/10/21.
 */
public class StatusBarUtils {

    //-------------------------------------------公有方法分割线--------------------------------------------

    /**
     * 设置内容全屏,即内容延伸至状态栏底部,状态栏文字还在
     *
     * @param activity
     */
    public static void setFullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置透明状态栏,这样才能让 ContentView 向上
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * 改变状态栏颜色
     *
     * @param activity
     * @param drawable
     */
    public static void setTranslucentStatusBar(Activity activity, Drawable drawable) {
        setTranslucentStatusBar(activity, drawable, 0);
    }


    public static void setTranslucentStatusBar(Activity activity, Drawable drawable, int alpha) {
        setFullScreen(activity);
        addStatusBarBehind(activity, drawable);
        setDecorStatusBarView(activity, alpha);
    }


    /**
     * 设置全屏并在decorView下加一个黑色并可以设置alpha的
     * view
     *
     * @param activity
     * @param alpha    （0~255）
     */
    public static void setTranslucentImageHeader(Activity activity, @IntRange(from = 0, to = 255) int alpha) {
        setTranslucentImageHeader(activity, alpha, null);
    }

    /**
     * @param activity
     * @param alpha          （0~255）
     * @param needOffsetView
     */
    public static void setTranslucentImageHeader(Activity activity, @IntRange(from = 0, to = 255) int alpha, View needOffsetView) {
        setFullScreen(activity);
        setDecorStatusBarView(activity, alpha);
        if (needOffsetView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) needOffsetView.getLayoutParams();
            layoutParams.setMargins(0, getStatusBarHeight(activity), 0, 0);
        }
    }


    /**
     * 使用此方法记得再根布局添加
     * <p>
     * android:fitsSystemWindows="true"
     *
     * @param activity
     * @param drawer
     * @param drawable 填充状态栏顶部的drawable
     * @param alpha    状态栏顶部黑色的透明度
     */
    public static void setTranslucentDrawerLayout(Activity activity, DrawerLayout drawer, Drawable drawable, @IntRange(from = 0, to = 255) int alpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // 生成一个状态栏大小的矩形
        // 添加 statusBarView 到布局中
        ViewGroup contentLayout = (ViewGroup) drawer.getChildAt(0);

        if (drawable != null) {
            if (contentLayout.getChildCount() > 0 && contentLayout.getChildAt(0) instanceof StatusBarUtils.StatusBarView) {
                contentLayout.getChildAt(0).setBackground(drawable);
            } else {
                StatusBarUtils.StatusBarView view = new StatusBarUtils.StatusBarView(activity);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity)));
                view.setBackground(drawable);
                contentLayout.addView(view, 0);
            }
            // 内容布局不是 LinearLayout 时,设置padding top
            if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1)
                        .setPadding(contentLayout.getPaddingLeft(), getStatusBarHeight(activity) + contentLayout.getPaddingTop(),
                                contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
            }
        }

        //ViewGroup contentView = (ViewGroup) drawer.getChildAt(0);
        ViewGroup nav = (ViewGroup) drawer.getChildAt(1);
        drawer.setFitsSystemWindows(false);
        contentLayout.setFitsSystemWindows(false);
        contentLayout.setClipToPadding(true);
        nav.setFitsSystemWindows(false);

        setDecorStatusBarView(activity, alpha);
    }

    /**
     * 使用此方法记得再根布局添加
     * <p>
     * android:fitsSystemWindows="true"
     *
     * @param activity
     * @param drawer
     * @param alpha    状态栏顶部黑色的透明度
     */
    public static void setTranslucentDrawerLayout(Activity activity, DrawerLayout drawer, @IntRange(from = 0, to = 255) int alpha) {
        setTranslucentDrawerLayout(activity, drawer, null, alpha);
    }

    /**
     * 强制改变顶部状态栏的颜色和透明度
     *
     * @param activity
     * @param alpha
     * @param drawable
     */
    public static void setStatusBarAlpha2Color(Activity activity, @IntRange(from = 0, to = 255) int alpha, Drawable drawable) {
        setDecorStatusBarView(activity, alpha, drawable);
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度px
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    //----------------------------------------------私有方法分割线-------------------------------------------

    private static void setDecorStatusBarView(Activity activity, @IntRange(from = 0, to = 255) int alpha) {
        //获取windowphone下的decorView
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        int count = decorView.getChildCount();
        //判断是否已经添加了statusBarView
        if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
            decorView.getChildAt(count - 1).getBackground().setAlpha(alpha);
        } else {
            //新建一个和状态栏高宽的view
            Drawable drawable = new ColorDrawable(Color.BLACK);
            StatusBarView statusView = createStatusBarView(activity, drawable, alpha);
            decorView.addView(statusView);
        }
    }

    /**
     * 强制性改变
     *
     * @param activity
     * @param alpha
     * @param drawable
     */
    private static void setDecorStatusBarView(Activity activity, @IntRange(from = 0, to = 255) int alpha, Drawable drawable) {
        //获取windowphone下的decorView
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        int count = decorView.getChildCount();
        //判断是否已经添加了statusBarView
        if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
            StatusBarView statusBarView = (StatusBarView) decorView.getChildAt(count - 1);
            statusBarView.setBackgroundDrawable(drawable);
            statusBarView.getBackground().setAlpha(alpha);
        } else {
            //新建一个和状态栏高宽的view
            StatusBarView statusView = createStatusBarView(activity, drawable, alpha);
            decorView.addView(statusView);
        }
    }

    /**
     * 创建一个和状态栏高度一样的view
     *
     * @param activity
     * @param drawable 背景drawable
     * @param alpha    透明度
     * @return
     */
    private static StatusBarView createStatusBarView(Activity activity, Drawable drawable, @IntRange(from = 0, to = 255) int alpha) {
        // 绘制一个和状态栏一样高的矩形
        StatusBarView statusBarView = new StatusBarView(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        drawable.setAlpha(alpha);
        statusBarView.setBackgroundDrawable(drawable);
        return statusBarView;
    }

    /**
     * 设置根布局参数
     */
    private static void setRootView(Activity activity) {
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        //rootview不会为状态栏流出状态栏空间
        ViewCompat.setFitsSystemWindows(rootView, true);
        rootView.setClipToPadding(true);
    }

    /**
     * 添加一个
     *
     * @param activity
     * @param drawable
     */
    private static void addStatusBarBehind(Activity activity, Drawable drawable) {
        ContentFrameLayout content = (ContentFrameLayout) activity.findViewById(android.R.id.content);
        int count = content.getChildCount();
        if (count > 0 && content.getChildAt(count - 1) instanceof StatusBarView) {
            content.getChildAt(count - 1).setBackgroundDrawable(drawable);
        } else {
            //新建一个和状态栏高宽的view
            StatusBarView statusView = createStatusBarView(activity, drawable, 255);
            content.addView(statusView);
        }
        setRootView(activity);
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private static int calculateStatusColor(int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    // ------------------------------------废弃方法---------------------------------------

    /**
     * @param activity
     * @param color
     * @param statusBarAlpha
     * @deprecated 已废弃  请使用
     * {{@link #setTranslucentStatusBar(Activity, Drawable, int)}}
     */
    @Deprecated
    public static void setColor(Activity activity, @ColorInt int color, int statusBarAlpha) {
        setTranslucentStatusBar(activity, new ColorDrawable(color), statusBarAlpha);
    }

    /**
     * @deprecated 添加了一个状态栏(实际上是个view)，放在了状态栏的垂直下方
     * {{@link #addStatusBarBehind(Activity, Drawable)}}
     */
    @Deprecated
    private static void addStatusBarBehind(Activity activity, @ColorInt int color, int statusBarAlpha) {
        ViewGroup decorView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        int count = decorView.getChildCount();
        //判断是否已经添加了statusBarView
        if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
            decorView.getChildAt(count - 1).setBackgroundDrawable(new ColorDrawable(color));
        } else {
            //新建一个和状态栏高宽的view
            StatusBarView statusView = createStatusBarView(activity, new ColorDrawable(color), statusBarAlpha);
            decorView.addView(statusView);
        }
        setRootView(activity);
    }


    public static class StatusBarView extends View {

        public StatusBarView(Context context) {
            super(context);
        }

        public StatusBarView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public StatusBarView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }
    }
}
