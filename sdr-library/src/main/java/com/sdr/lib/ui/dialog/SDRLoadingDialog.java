package com.sdr.lib.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sdr.lib.R;
import com.sdr.lib.util.CommonUtil;

/**
 * Created by Administrator on 2018/5/18.
 */

public class SDRLoadingDialog extends AlertDialog {

    private final Builder mBuilder;

    private SDRLoadingDialog(Context context, Builder builder) {
        super(context);
        mBuilder = builder;
    }

    private SDRLoadingDialog(Context context, int themeResId, Builder builder) {
        super(context, themeResId);
        mBuilder = builder;
    }

    public void setContent(String content) {
        mBuilder.setContent(content);
    }

    public void setContent(String content, boolean showProgress) {
        mBuilder.setContent(content, showProgress);
    }


    /**
     * 基础的Builder
     */
    public static final class Builder {
        private Context context;
        private LayoutInflater layoutInflater;

        private SDRLoadingDialog dialog;
        private ProgressBar progressBar;
        private TextView textView;

        public Builder(Context context) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        private boolean blur = true;
        private boolean cancel = false;
        private String content;

        public Builder blur(boolean blur) {
            this.blur = blur;
            return this;
        }

        public Builder cancel(boolean cancel) {
            this.cancel = cancel;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public SDRLoadingDialog build() {
            dialog = new SDRLoadingDialog(context, blur ? R.style.SDR_Theme_Dialog : R.style.SDR_Theme_Dialog_NoBlur, this);
            View view = layoutInflater.inflate(R.layout.sdr_layout_public_loading_dialog, null, false);
            LinearLayout container = view.findViewById(R.id.hyf_loading_progress_container);
            progressBar = view.findViewById(R.id.hyf_loading_progress);
            textView = view.findViewById(R.id.hyf_loading_progress_tv_content);
            dialog.setView(view);
            dialog.setCancelable(cancel);

            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.BLACK);
            drawable.setAlpha(150);
            drawable.setCornerRadius(CommonUtil.dip2px(context, 5));
            container.setBackgroundDrawable(drawable);
            if (TextUtils.isEmpty(content)) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(content);
            }
            return dialog;
        }


        private void setContent(String content) {
            setContent(content, true);
        }


        /**
         * 设置
         *
         * @param content
         * @param showProgress
         */
        private void setContent(String content, boolean showProgress) {
            if (dialog == null || progressBar == null || textView == null) return;
            if (showProgress) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }

            if (textView.getVisibility() != View.VISIBLE) {
                textView.setVisibility(View.VISIBLE);
            }
            textView.setText(content);

            if (!dialog.isShowing()) {
                dialog.show();
            }
        }

    }


//    private abstract static class BaseBuilder<T> {
//        private T t;
//
//        private Context mContext;
//        private LinearLayout contentView;
//        private View progressView;
//        private TextView textView;
//        private SDRLoadingDialog dialog;
//
//        private LayoutInflater mLayoutInflater;
//
//        public BaseBuilder(Context context) {
//            mContext = context;
//            mLayoutInflater = LayoutInflater.from(context);
//            t = newInstance();
//        }
//
//
//        private boolean isBlur;
//
//        public T blur(boolean blur) {
//            isBlur = blur;
//            return t;
//        }
//
//        private String title = "";
//
//        public T content(String title) {
//            this.title = title;
//            return t;
//        }
//
//        private int duration = 1500;
//
//        public T duration(int duration) {
//            this.duration = duration;
//            return t;
//        }
//
//        private int color = COLOR_DEFAULT;
//
//        public T color(int color) {
//            this.color = color;
//            return t;
//        }
//
//        private boolean cancel = true;
//
//        public T cancel(boolean cancel) {
//            this.cancel = cancel;
//            return t;
//        }
//
//
//        public SDRLoadingDialog build() {
//            dialog = new SDRLoadingDialog(mContext, isBlur ? R.style.SDR_Theme_Dialog : R.style.SDR_Theme_Dialog_NoBlur, this);
//            View view = mLayoutInflater.inflate(R.layout.sdr_layout_public_loading_dialog, null, false);
//            contentView = view.findViewById(R.id.hyf_loading_progress_container);
//            progressView = findViewById(view);
//            progressView.setVisibility(View.VISIBLE);
//            textView = view.findViewById(R.id.hyf_loading_progress_tv_content);
//
//            initOtherSetting(progressView);
//
//            if (t instanceof MaterialBuilder) {
//                MaterialProgressBar progressBar = (MaterialProgressBar) progressView;
//                // 设置颜色
//                if (color != COLOR_DEFAULT)
//                    progressBar.setIndeterminateTintList(ColorStateList.valueOf(color));
//            } else if (t instanceof RingBuilder) {
//                RingImageView ringImageView = (RingImageView) progressView;
//                if (color != COLOR_DEFAULT)
//                    ringImageView.setFillColor(color);
//                ringImageView.setAnimDuration(duration);
//            } else if (t instanceof ImageBuilder) {
//                ImageView imageView = (ImageView) progressView;
//                if (color != COLOR_DEFAULT)
//                    imageView.setColorFilter(color, PorterDuff.Mode.SRC_IN);
//                setRotateAnim(imageView, duration);
//            }
//
//            // 设置背景和textview的文字
//            if (!TextUtils.isEmpty(title)) {
//                textView.setText(title);
//                textView.setVisibility(View.VISIBLE);
//            } else {
//                textView.setVisibility(View.GONE);
//            }
//            contentView.setBackgroundResource(R.drawable.sdr_shape_bg_loading_dialog);
//            dialog.setView(view);
//            dialog.setCancelable(cancel);
//            return dialog;
//        }
//
//        protected void initOtherSetting(View progressView) {
//
//        }
//
//        /**
//         * —————————————————————工具方法———————————————————————
//         */
//        private final void setRotateAnim(ImageView imageView, long duration) {
//            imageView.clearAnimation();
//            RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
//                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            rotateAnimation.setDuration(duration);
//            rotateAnimation.setRepeatCount(Animation.INFINITE);
//            LinearInterpolator lin = new LinearInterpolator();
//            rotateAnimation.setInterpolator(lin);
//            imageView.setAnimation(rotateAnimation);
//        }
//
//
//        /**
//         * —————————————————————抽象方法—————————————————————————
//         */
//        abstract T newInstance();
//
//        abstract View findViewById(View view);
//    }
//
//    public static final class MaterialBuilder extends BaseBuilder<MaterialBuilder> {
//
//        public MaterialBuilder(Context context) {
//            super(context);
//        }
//
//        @Override
//        MaterialBuilder newInstance() {
//            return this;
//        }
//
//        @Override
//        View findViewById(View view) {
//            return view.findViewById(R.id.hyf_progress);
//        }
//    }
//
//    public static final class RingBuilder extends BaseBuilder<RingBuilder> {
//        public RingBuilder(Context context) {
//            super(context);
//        }
//
//        private int ringSize;
//
//        public RingBuilder ringSize(int dp) {
//            ringSize = dp;
//            return this;
//        }
//
//        @Override
//        RingBuilder newInstance() {
//            return this;
//        }
//
//        @Override
//        View findViewById(View view) {
//            return view.findViewById(R.id.hyf_iv_ring);
//        }
//
//        @Override
//        protected void initOtherSetting(View progressView) {
//            RingImageView ringImageView = (RingImageView) progressView;
//            ringImageView.setRingWidth(ringSize);
//        }
//    }
//
//    public static final class ImageBuilder extends BaseBuilder<ImageBuilder> {
//        private int imageRes = R.drawable.sdr_ic_dialog_simple_progress;
//
//        public ImageBuilder(Context context) {
//            super(context);
//        }
//
//        public ImageBuilder imageRes(int imageRes) {
//            this.imageRes = imageRes;
//            return this;
//        }
//
//        @Override
//        ImageBuilder newInstance() {
//            return this;
//        }
//
//        @Override
//        View findViewById(View view) {
//            return view.findViewById(R.id.hyf_iv_loading);
//        }
//
//        @Override
//        protected void initOtherSetting(View progressView) {
//            ImageView imageView = (ImageView) progressView;
//            imageView.setImageResource(imageRes);
//        }
//    }

}
