package com.sdr.lib.ui.toast;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sdr.lib.util.CommonUtil;

/**
 * Created by  HYF on 2018/7/16.
 * Email：775183940@qq.com
 * 悬浮窗
 */

public class ToastSimple {
    private static final int HIDE_SNACK_TOAST = 2;
    private Context mContext;

    private WindowManager mWm;
    private WindowManager.LayoutParams mParams;
    private TextView mView;
    private Handler mHandler;


    private int background = Color.parseColor("#999999"); // 背景颜色  默认灰色
    private int radius = 0; // 圆角
    private int textColor = Color.WHITE; // 字体颜色 默认白色
    private String text = ""; // 默认文字
    private long duration = 3000; // 默认显示 3s

    public ToastSimple(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR | WindowManager.LayoutParams.TYPE_PHONE;
        }
        mParams.format = PixelFormat.TRANSLUCENT;
//        mParams.windowAnimations = com.android.R.style.Animation_Toast;
        mParams.windowAnimations = android.R.style.Animation_Toast;
        mParams.setTitle("Toast");
        // 位置属性
        mParams.gravity = Gravity.CENTER | Gravity.BOTTOM;  // 左上
        mParams.y = CommonUtil.dip2px(mContext, 60);

        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mView = new TextView(mContext);
        int padding = CommonUtil.dip2px(mContext, 15);
        mView.setPadding(padding, padding / 2, padding, padding / 2);
        mView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
    }

    public ToastSimple background(int backgroundColor) {
        this.background = backgroundColor;
        return this;
    }

    public ToastSimple textColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public ToastSimple radius(int radius) {
        this.radius = radius;
        return this;
    }

    public ToastSimple text(String text) {
        if (text == null) text = "";
        this.text = text;
        return this;
    }

    public ToastSimple duration(long duration) {
        this.duration = duration;
        return this;
    }

    // show之前需要授权
    public void show() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(mContext)) {
                Toast.makeText(mContext, "请授权允许出现在其他应用上", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } else {
                create();
            }
        } else {
            create();
        }
    }


    private void create() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(background);
        drawable.setCornerRadius(radius);
        mView.setBackgroundDrawable(drawable);
        mView.setTextColor(textColor);
        mView.setText(text);
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == HIDE_SNACK_TOAST) {
                        hide();
                    }
                }
            };
        }
        mHandler.removeMessages(HIDE_SNACK_TOAST);
        mHandler.sendEmptyMessageDelayed(HIDE_SNACK_TOAST, duration);

        if (mView != null) {
            if (mView.getParent() == null) {
                // 添加到窗体管理器中才能显示出来
                mWm.addView(mView, mParams);
            } else {

            }
        }
    }

    private void hide() {
        if (mView != null) {
            if (mView.getParent() != null) {
                mWm.removeView(mView);
                mHandler = null;
            }
        }
    }
}
