package com.sdr.lib.support.update;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.sdr.lib.R;
import com.sdr.lib.ui.dialog.SDRSimpleDialog;
import com.sdr.lib.util.CommonUtil;

import java.io.File;

/**
 * Created by HyFun on 2018/10/30.
 * Email:775183940@qq.com
 */

public class UpdateAppManager {

    public static final void checkUpdate(Context context, boolean showDialog, AppNeedUpdateListener needUpdateListener) {
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return;
        }

        if (info.metaData == null) {
            return;
        }
        String apiKey = info.metaData.getString("ApiKey");
        String appKey = info.metaData.getString("AppKey");
        if (TextUtils.isEmpty(apiKey) || TextUtils.isEmpty(appKey)) {
            return;
        }
        UpdatePresenter presenter = new UpdatePresenter(new UpdateImp(context, showDialog, needUpdateListener));
        presenter.check(apiKey, appKey, CommonUtil.getPackageInfo(context).versionName);
    }


    private static class UpdateImp implements UpdateView {
        private Context context;
        private boolean showDialog;
        private AppNeedUpdateListener needUpdateListener;

        private String appName;

        public UpdateImp(Context context, boolean showDialog, AppNeedUpdateListener needUpdateListener) {
            this.context = context;
            this.showDialog = showDialog;
            this.needUpdateListener = needUpdateListener;
            appName = context.getResources().getString(R.string.app_name);
        }

        private SDRSimpleDialog simpleDialog;

        @Override
        public void showCheckDialog(String message) {
            if (showDialog) {
                if (simpleDialog == null) {
                    simpleDialog = new SDRSimpleDialog.Builder(context)
                            .cancel(false)
                            .blur(true)
                            .content(message)
                            .build();
                }
                simpleDialog.show();
            }
        }

        @Override
        public void showCheckDialogResult(String message) {
            if (showDialog) {
                if (simpleDialog != null) {
                    simpleDialog.setContent(message);
                    simpleDialog.setCancel(true);
                }
            }
        }

        @Override
        public void hideCheckDialog() {
            if (simpleDialog != null) {
                simpleDialog.dismiss();
            }
        }

        @Override
        public void isNeedUpdate(boolean need) {
            if (needUpdateListener != null) {
                needUpdateListener.isNeedUpdate(need);
            }
        }

        @Override
        public void showUpdateDialog(final UpdatePresenter presenter, final UpdateInfo.DataBean data) {
            // 显示更新提示框
            // 首先判断该activity是否已经下载了
            final String apkName = appName + "_" + data.getBuildVersion() + ".apk";
            final String savePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            final File localFile = getLocalExistFile(apkName, savePath);

            new SDRUpdateDialog.Builder(context)
                    .title(localFile == null ? "是否更新到" + appName + data.getBuildVersion() + "(" + data.getBuildVersionNo() + ")版本？" : "已下载好" + appName + data.getBuildVersion() + "(" + data.getBuildVersionNo() + ")版本，是否安装？")
                    .content(data.getBuildUpdateDescription())
                    .positiveText(localFile == null ? "立即更新" : "立即安装")
                    .listener(new SDRUpdateDialog.OnclickUpdateListener() {
                        @Override
                        public void clickUpdate(SDRUpdateDialog dialog) {
                            if (localFile == null) {
                                presenter.downLoadApk(data.getDownloadURL(), apkName, savePath);
                            } else {
                                CommonUtil.installApk(context, localFile);
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void clickBrowser(SDRUpdateDialog dialog) {
                            // 跳转到浏览器去下载
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            Uri content_url = Uri.parse(data.getBuildShortcutUrl());
                            intent.setData(content_url);
                            context.startActivity(intent);
                            dialog.dismiss();
                        }
                    })
                    .build()
                    .show();
        }


        private SDRUpdateDownloadDialog downloadDialog;

        @Override
        public void showDownLoadDilog(String message) {
            if (downloadDialog == null) {
                downloadDialog = new SDRUpdateDownloadDialog.Builder(context)
                        .build();
            }
            downloadDialog.show();
        }

        @Override
        public void onDownLoadProgress(int progress) {
            if (downloadDialog != null) {
                downloadDialog.setProgress(progress);
            }
        }

        @Override
        public void onDownLoadSuccess(File file) {
            if (downloadDialog != null) {
                downloadDialog.dismiss();
            }
            // 安装app
            CommonUtil.installApk(context, file);
        }

        @Override
        public void onDownLoadFailed(String message) {
            if (downloadDialog != null) {
                downloadDialog.setError(message);
            }
        }

    }


    // ———————————————————————私有方法—————————————————————

    /**
     * 通过URL获取文件名
     *
     * @param url
     * @return
     */
    private static final String getFileNameByUrl(String url) {
        String filename = url.substring(url.lastIndexOf("/") + 1);
        filename = filename.substring(0, filename.indexOf("?") == -1 ? filename.length() : filename.indexOf("?"));
        return filename;
    }


    private static final File getLocalExistFile(String fileName, String path) {
        File file = null;
        File parentDir = new File(path);
        if (parentDir.isDirectory()) {
            File[] files = parentDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (fileName.equals(files[i].getName())) {
                    file = files[i];
                    return file;
                }
            }
        }
        return file;
    }
}
