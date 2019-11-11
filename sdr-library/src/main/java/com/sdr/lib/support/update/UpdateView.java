package com.sdr.lib.support.update;

import java.io.File;

/**
 * Created by HyFun on 2018/10/30.
 * Email:775183940@qq.com
 */

interface UpdateView {
    void showCheckDialog(String message);

    void showCheckDialogResult(String message);

    void hideCheckDialog();

    void isNeedUpdate(boolean need);

    void showUpdateDialog(UpdatePresenter presenter, UpdateInfo.DataBean data);

    void showDownLoadDilog(String message);

    void onDownLoadProgress(int progress);

    void onDownLoadSuccess(File file);

    void onDownLoadFailed(String message);
}
