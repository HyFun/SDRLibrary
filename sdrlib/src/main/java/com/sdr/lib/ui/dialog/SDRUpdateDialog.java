package com.sdr.lib.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdr.lib.R;


/**
 * Created by Administrator on 2018/5/22.
 */

public class SDRUpdateDialog extends AlertDialog {
    private static final int COLOR_DEFAULT = -2;

    private SDRUpdateDialog(@NonNull Context context) {
        super(context);
    }

    private SDRUpdateDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public final static class Builder {
        private Context mContext;

        public Builder(Context context) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        private LayoutInflater mLayoutInflater;

        private String title;
        private String content;
        private boolean blur = true;
        private OnclickUpdateListener listener;
        private boolean cancel;
        private String positiveText;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder blur(boolean blur) {
            this.blur = blur;
            return this;
        }

        public Builder listener(OnclickUpdateListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder cancel(boolean cancel) {
            this.cancel = cancel;
            return this;
        }

        public Builder positiveText(String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public SDRUpdateDialog build() {
            View view = mLayoutInflater.inflate(R.layout.sdr_layout_public_update_dialog, null);
            final SDRUpdateDialog sdrUpdateDialog = new SDRUpdateDialog(mContext, blur ? R.style.SDR_Theme_Dialog : R.style.SDR_Theme_Dialog_NoBlur);
            ImageView ivClose = view.findViewById(R.id.hyf_dialog_update_iv_close);
            TextView tvTitle = view.findViewById(R.id.hyf_dialog_update_tv_title);
            TextView tvContent = view.findViewById(R.id.hyf_dialog_update_tv_content);
            Button button = view.findViewById(R.id.hyf_dialog_update_btn_update);
            Button browser = view.findViewById(R.id.hyf_dialog_update_btn_browser);
            if (!TextUtils.isEmpty(title)) tvTitle.setText(title);
            if (!TextUtils.isEmpty(content)) tvContent.setText(content);
            if (!TextUtils.isEmpty(positiveText)) button.setText(positiveText);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.clickUpdate(sdrUpdateDialog);
                }
            });

            browser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.clickBrowser(sdrUpdateDialog);
                    }
                }
            });

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sdrUpdateDialog.dismiss();
                }
            });
            sdrUpdateDialog.setView(view);
            sdrUpdateDialog.setCancelable(cancel);
            return sdrUpdateDialog;
        }

    }

    public interface OnclickUpdateListener {
        void clickUpdate(SDRUpdateDialog dialog);

        void clickBrowser(SDRUpdateDialog dialog);
    }
}
