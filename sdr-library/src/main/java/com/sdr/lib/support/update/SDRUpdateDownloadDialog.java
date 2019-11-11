package com.sdr.lib.support.update;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdr.lib.R;
import com.sdr.lib.util.CommonUtil;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 *  下载APP显示下载进度的dialog
 */

class SDRUpdateDownloadDialog extends AlertDialog {
    private Builder mBuilder;

    private SDRUpdateDownloadDialog(@NonNull Context context, Builder builder) {
        super(context);
        mBuilder = builder;
    }

    private SDRUpdateDownloadDialog(@NonNull Context context, int themeResId, Builder builder) {
        super(context, themeResId);
        mBuilder = builder;
    }

    public void setProgress(int progress) {
        mBuilder.updateProgress(progress);
    }

    public void setError(String errorMsg) {
        mBuilder.error(errorMsg);
        setCancelable(true);
    }

    public static final class Builder {
        private Context mContext;

        private LayoutInflater mLayoutInflater;
        private MaterialProgressBar mProgressBar;
        private TextView mTvProgress;

        public Builder(Context context) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        private boolean blur = true;

        public Builder blur(boolean blur) {
            this.blur = blur;
            return this;
        }

        public SDRUpdateDownloadDialog build() {
            SDRUpdateDownloadDialog sdrUpdateDownloadDialog = new SDRUpdateDownloadDialog(mContext, blur ? R.style.SDR_Theme_Dialog : R.style.SDR_Theme_Dialog_NoBlur, this);
            View view = mLayoutInflater.inflate(R.layout.sdr_layout_public_update_dialog_download, null);
            ImageView ivClose = view.findViewById(R.id.hyf_dialog_update_iv_close);
            mTvProgress = view.findViewById(R.id.hyf_dialog_update_download_tv_progress);
            mProgressBar = view.findViewById(R.id.hyf_dialog_update_download_progress);
            ivClose.setVisibility(View.INVISIBLE);
            int space = CommonUtil.dip2px(mContext, 10);
            sdrUpdateDownloadDialog.setView(view, space, space, space, space);
            sdrUpdateDownloadDialog.setCancelable(false);
            return sdrUpdateDownloadDialog;
        }

        private void updateProgress(int progress) {
            if (mProgressBar == null) return;
            if (progress < 0) progress = 0;
            if (progress > 100) progress = 100;
            mProgressBar.setProgress(progress);
            mTvProgress.setText(String.format("%02d", progress) + "%");
        }

        private void error(String errorMsg) {
            if (mTvProgress == null) return;
            mTvProgress.setText(errorMsg);
        }

    }

}
