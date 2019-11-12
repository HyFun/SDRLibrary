package com.sdr.sdrlib.base;

import android.view.View;

import com.sdr.lib.mvp.AbstractView;
import com.sdr.lib.ui.dialog.SDRLoadingDialog;
import com.sdr.lib.util.AlertUtil;
import com.sdr.sdrlib.util.AppUtil;

import butterknife.ButterKnife;

/**
 * Created by HyFun on 2018/11/06.
 * Email:775183940@qq.com
 */

public class BaseActivity extends com.sdr.lib.base.BaseActivity implements AbstractView {

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    // —————————————————————VIEW———————————————————————

    private SDRLoadingDialog progressDialog;

    @Override
    public void showLoadingDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = AppUtil.create(getContext())
                    .content(msg)
                    .build();
        }
        progressDialog.setContent(msg);
    }

    @Override
    public void hideLoadingDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void showSuccessMsg(String msg, String content) {
        AlertUtil.showPositiveToastTop(msg, content);
    }

    @Override
    public void showErrorMsg(String msg, String content) {
        AlertUtil.showNegativeToastTop(msg, content);
    }

    @Override
    public void showNormalMsg(String msg, String content) {
        AlertUtil.showNormalToastTop(msg, content);
    }
}
