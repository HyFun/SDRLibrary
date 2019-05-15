package com.sdr.lib.mvp;

/**
 * Created by HyFun on 2018/10/11.
 * Email:775183940@qq.com
 */

public interface AbstractView {

    void showLoadingDialog(String msg);

    void hideLoadingDialog();

    void showSuccessMsg(String msg);

    void showErrorMsg(String msg);

    void showNormalMsg(String msg);

    void showSuccessToast(String msg);

    void showErrorToast(String msg);

    void showNormalToast(String msg);

}
