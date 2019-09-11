package com.sdr.lib.ui.toast;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdr.lib.R;


/**
 * Created by Administrator on 2017/11/16.
 * 悬浮窗
 */

public class ToastTop {
    private static final int HIDE_SNACK_TOAST = 0;


    private Context mContext;

    private WindowManager mWm;
    private WindowManager.LayoutParams mParams;
    private View mView;

    private LayoutInflater mLayoutInflater;

    // 视图
    private View contentView;
    private ImageView mImageView;
    private TextView mTextView;

    /**
     * 逻辑
     */
    private int iconRes;
    private int iconColor = Color.BLACK;
    private String title = "";
    private int titleColor = Color.BLACK;
    // 显示时间
    private long showTime = 2000;


    public ToastTop(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR | WindowManager.LayoutParams.TYPE_PHONE;
        }
        mParams.setTitle("Toast");
        // 设置吐司窗体的标识
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        // 透明，不透明会出现重叠效果
        mParams.format = PixelFormat.TRANSLUCENT;
        // 位置属性
        mParams.gravity = Gravity.TOP ;  // 左上
        //出现动画
        //mParams.windowAnimations = R.style.Animation_Snack_Toast;
        mParams.windowAnimations = android.R.style.Animation_Toast;

        // 初始化吐司窗口布局
        mView = mLayoutInflater.inflate(R.layout.sdr_layout_public_snack_top_bar, null, false);
        contentView = (LinearLayout) mView.findViewById(R.id.hyf_snackbar_view);
        mImageView = (ImageView) mView.findViewById(R.id.hyf_iv_sanckbar);
        mTextView = (TextView) mView.findViewById(R.id.hyf_tv_sanckbar);
//        // 设置margintop
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            LinearLayout parent = (LinearLayout) contentView.getChildAt(0);
//            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) parent.getLayoutParams();
//            params.topMargin = getStatusBarHeight(mContext);
//            parent.setLayoutParams(params);
//            mView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }
    }

    private Handler mHandler;

    public ToastTop setIconRes(int iconRes) {
        this.iconRes = iconRes;
        return this;
    }

    public ToastTop setTitle(String title) {
        this.title = title;
        return this;
    }

    public ToastTop setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public ToastTop setShowTime(long showTime) {
        this.showTime = showTime;
        return this;
    }

    public ToastTop setIconColor(int iconColor) {
        this.iconColor = iconColor;
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
        if (iconRes != 0) {
            Drawable drawable = mContext.getResources().getDrawable(iconRes);
            drawable.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
            mImageView.setImageDrawable(drawable);
        }
        mImageView.setVisibility(iconRes == 0 ? View.GONE : View.VISIBLE);
        mTextView.setText(title);
        mTextView.setTextColor(titleColor);

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
        mHandler.sendEmptyMessageDelayed(HIDE_SNACK_TOAST, showTime);

        contentView.setOnClickListener(mClickListener);

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

    // 点击contentview让view消失的点击事件
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mHandler == null) return;
            mHandler.removeMessages(HIDE_SNACK_TOAST);
            hide();
        }
    };
}
