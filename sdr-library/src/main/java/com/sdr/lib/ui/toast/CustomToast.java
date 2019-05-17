package com.sdr.lib.ui.toast;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sdr.lib.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by HyFun on 2019/05/17.
 * Email: 775183940@qq.com
 * Description:
 */

public class CustomToast {

    private static final int SHOW = 1;
    private static final int HIDE = 0;


    private Context context;

    public CustomToast(Context context) {
        this.context = context;
        init();
    }


    private ImageView imageView;
    private TextView textView;
    private Toast mToast;
    private Object mTN;
    private Method mShow;
    private Method mHide;
    private Field mViewFeild;


    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.sdr_layout_public_toast_top_info, null);
        imageView = view.findViewById(R.id.sdr_public_toast_iv_image);
        textView = view.findViewById(R.id.sdr_public_toast_tv_title);
        mToast = new Toast(context);
        mToast.setView(view);
        mToast.setGravity(Gravity.TOP, 0, 0);
        reflectToast();
    }

    private int iconRes = 0;
    private int iconColor = 0;
    private String title = "";
    private long duration = 3000;

    public CustomToast setIconRes(int iconRes) {
        this.iconRes = iconRes;
        return this;
    }

    public CustomToast setIconColor(int iconColor) {
        this.iconColor = iconColor;
        return this;
    }

    public CustomToast setTitle(String title) {
        this.title = title;
        return this;
    }

    public CustomToast setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public void show() {
        // 先移除
        handler.removeMessages(SHOW);
        handler.removeMessages(HIDE);

        if (iconRes != 0) {
            Drawable drawable = context.getResources().getDrawable(iconRes);
            drawable.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
            imageView.setImageDrawable(drawable);
        }
        textView.setText(title);
        if (mShow != null && mHide != null) {
            handler.sendEmptyMessage(SHOW);
            Logger.e("使用handler");
        } else {
            mToast.show();
            Logger.e("使用自身show");
        }
    }

    // ———————————————————————————————私有方法———————————————————————————————————

    private void reflectEnableClick() {
        try {
            Object mTN;
            mTN = getField(mToast, "mTN");
            if (mTN != null) {
                Object mParams = getField(mTN, "mParams");
                if (mParams != null
                        && mParams instanceof WindowManager.LayoutParams) {
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
                    //显示与隐藏动画
//                    params.windowAnimations = R.style.ClickToast;
                    //Toast可点击
                    params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                    //设置viewgroup宽高
                    params.width = WindowManager.LayoutParams.MATCH_PARENT; //设置Toast宽度为屏幕宽度
                    params.height = WindowManager.LayoutParams.WRAP_CONTENT; //设置高度
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反射字段
     *
     * @param object    要反射的对象
     * @param fieldName 要反射的字段名称
     */
    private static Object getField(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        if (field != null) {
            field.setAccessible(true);
            return field.get(object);
        }
        return null;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW:
                    showToast();
                    handler.sendEmptyMessageDelayed(HIDE, duration);
                    break;
                case HIDE:
                    hide();
                    break;
            }
        }
    };

    private void reflectToast() {
        Field field = null;
        try {
            field = mToast.getClass().getDeclaredField("mTN");
            field.setAccessible(true);
            mTN = field.get(mToast);
            mShow = mTN.getClass().getDeclaredMethod("show");
            mHide = mTN.getClass().getDeclaredMethod("hide");
            mViewFeild = mTN.getClass().getDeclaredField("mNextView");
            mViewFeild.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        }
    }

    private void showToast() {
        try {
            //android4.0以上就要以下处理
            if (Build.VERSION.SDK_INT > 14) {
                Field mNextViewField = mTN.getClass().getDeclaredField("mNextView");
                mNextViewField.setAccessible(true);
                LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = mToast.getView();
                mNextViewField.set(mTN, v);
                Method method = mTN.getClass().getDeclaredMethod("show", null);
                method.invoke(mTN, null);
            }
            mShow.invoke(mTN, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hide() {
        try {
            mHide.invoke(mTN, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}
