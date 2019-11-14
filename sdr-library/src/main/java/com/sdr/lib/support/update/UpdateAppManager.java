package com.sdr.lib.support.update;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.sdr.lib.ui.dialog.SDRSimpleDialog;
import com.sdr.lib.util.CommonUtil;

import java.io.File;

/**
 * Created by HyFun on 2018/10/30.
 * Email:775183940@qq.com
 */

public class UpdateAppManager {

    public static final void checkUpdate(Context context, boolean showDialog, AppNeedUpdateListener needUpdateListener) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (info.metaData == null) {
                Logger.e("请在application中设置meta-data AppName");
                return;
            }
            String appName = info.metaData.getString("AppName");
            if (TextUtils.isEmpty(appName)) {
                Logger.e("请在application中设置meta-data AppName");
                return;
            }
            UpdatePresenter presenter = new UpdatePresenter(new UpdateImp(context, context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), showDialog, needUpdateListener));
            presenter.check(appName, CommonUtil.getPackageInfo(context).versionCode);

        } catch (PackageManager.NameNotFoundException e) {
            return;
        }
    }


    private static class UpdateImp implements UpdateView {
        private Context context;
        private String savePath;
        private boolean showDialog;
        private AppNeedUpdateListener needUpdateListener;

        public UpdateImp(Context context, String savePath, boolean showDialog, AppNeedUpdateListener needUpdateListener) {
            this.context = context;
            this.savePath = savePath;
            this.showDialog = showDialog;
            this.needUpdateListener = needUpdateListener;
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
        public void showUpdateDialog(final UpdatePresenter presenter, String versionName, final String downLoadUrl, String updateDetail) {
            // 显示更新提示框
            // 首先判断该activity是否已经下载了
            final String apkName = getFileNameByUrl(downLoadUrl);
            final File localFile = getLocalExistFile(apkName, savePath);

            new SDRUpdateDialog.Builder(context)
                    .title(localFile == null ? "是否更新到" + versionName + "版本？" : "已下载好" + versionName + "版本，是否安装？")
                    .content(updateDetail)
                    .positiveText(localFile == null ? "立即更新" : "立即安装")
                    .listener(new SDRUpdateDialog.OnclickUpdateListener() {
                        @Override
                        public void clickUpdate(SDRUpdateDialog dialog) {
                            if (localFile == null) {
                                presenter.downLoadApk(downLoadUrl, apkName, savePath);
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
                            Uri content_url = Uri.parse(downLoadUrl);
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
