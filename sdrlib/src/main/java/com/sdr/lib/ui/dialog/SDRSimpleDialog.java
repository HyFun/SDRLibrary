package com.sdr.lib.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sdr.lib.R;
import com.sdr.lib.util.CommonUtil;


/**
 * Created by Administrator on 2018/5/18.
 */

public final class SDRSimpleDialog extends AlertDialog {
    private final Builder mBuilder;

    private SDRSimpleDialog(@NonNull Context context, Builder builder) {
        super(context);
        mBuilder = builder;
    }

    private SDRSimpleDialog(@NonNull Context context, int themeResId, Builder builder) {
        super(context, themeResId);
        mBuilder = builder;
    }

    // 设置结果
    public void setCancel(boolean cancel) {
        if (mBuilder.dialog != null)
            mBuilder.dialog.setCancelable(cancel);
    }

    public void setContent(String content) {
        setContent(content, false);
    }

    public void setContent(String content, boolean showProgress) {
        if (mBuilder.progressBar == null || mBuilder.textView == null) return;
        if (!isShowing()) show();
        mBuilder.progressBar.setVisibility(showProgress ? View.VISIBLE : View.GONE);
        mBuilder.textView.setText(content);
    }

    public static final class Builder {
        private Context mContext;
        private SDRSimpleDialog dialog;
        private ProgressBar progressBar;
        private TextView textView;

        private LayoutInflater mLayoutInflater;

        public Builder(Context context) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        private boolean isBlur = true;

        public Builder blur(boolean blur) {
            isBlur = blur;
            return this;
        }

        private boolean isCancel = false;

        public Builder cancel(boolean isCancel) {
            this.isCancel = isCancel;
            return this;
        }

        private String title = "";

        public Builder content(String title) {
            this.title = title;
            return this;
        }

        public SDRSimpleDialog build() {
            View view = mLayoutInflater.inflate(R.layout.sdr_layout_public_simple_dialog, null);
            progressBar = view.findViewById(R.id.hyf_progress);
            progressBar.setIndeterminate(true);
            textView = view.findViewById(R.id.hyf_textView);
            textView.setText(title);

            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.BLACK);
            drawable.setAlpha(150);
            drawable.setCornerRadius(CommonUtil.dip2px(mContext, 5));
            view.setBackgroundDrawable(drawable);


            dialog = new SDRSimpleDialog(mContext, isBlur ? R.style.SDR_Theme_Dialog : R.style.SDR_Theme_Dialog_NoBlur, this);
            dialog.setView(view);
            dialog.setCancelable(isCancel);
            return dialog;
        }
    }
}
