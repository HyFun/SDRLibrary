package com.sdr.lib.support.update;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.SpeedCalculator;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed;
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend;
import com.sdr.lib.http.HttpClient;
import com.sdr.lib.support.SDRAPI;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by HyFun on 2018/10/30.
 * Email:775183940@qq.com
 */

class UpdatePresenter {

    private SDRAPI sdrapi;
    private UpdateView view;

    public UpdatePresenter(UpdateView updateView) {
        this.view = updateView;

        sdrapi = HttpClient.getInstance().createRetrofit(
                SDRAPI.UPDATE_APP_URL,
                HttpClient.getInstance().getOkHttpClient(),
                SDRAPI.class
        );
    }

    /**
     * 检测更新
     *
     * @param appName
     * @param versionCode
     */
    public void check(String appName, int versionCode) {
        // 开始检测更新
        view.showCheckDialog("正在检测更新");

        sdrapi.checkUpdate(appName, versionCode)
                .compose(new ObservableTransformer<UpdateInfo, UpdateInfo>() {
                    @Override
                    public ObservableSource<UpdateInfo> apply(Observable<UpdateInfo> upstream) {
                        return upstream.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribeWith(new ResourceObserver<UpdateInfo>() {
                    @Override
                    public void onNext(UpdateInfo updateInfo) {
                        String downLoadUrl = updateInfo.getAppAddr();
                        view.isNeedUpdate(!TextUtils.isEmpty(downLoadUrl));
                        if (TextUtils.isEmpty(downLoadUrl)) {
                            // 说明是最新版本的app
                            view.showCheckDialogResult("当前应用已是最新版本");
                        } else {
                            // 说明需要更新app
                            view.hideCheckDialog();
                            view.showUpdateDialog(UpdatePresenter.this, updateInfo.getVersionName(), updateInfo.getAppAddr(), updateInfo.getUpdateDetail());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideCheckDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 下载文件 带进度
     *
     * @param url
     * @param fileName
     * @param savePath
     */
    public void downLoadApk(String url, String fileName, String savePath) {
        new DownloadTask.Builder(url, savePath, fileName)
                .setMinIntervalMillisCallbackProcess(30)
                .setPassIfAlreadyCompleted(false)
                .build()
                .enqueue(new DownloadListener4WithSpeed() {

                    private long totalLength;

                    @Override
                    public void taskStart(@NonNull DownloadTask task) {
                        view.showDownLoadDilog("正在准备更新APP");
                    }

                    @Override
                    public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaderFields) {

                    }

                    @Override
                    public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {

                    }

                    @Override
                    public void infoReady(@NonNull DownloadTask task, @NonNull BreakpointInfo info, boolean fromBreakpoint, @NonNull Listener4SpeedAssistExtend.Listener4SpeedModel model) {
                        totalLength = info.getTotalLength();
                    }

                    @Override
                    public void progressBlock(@NonNull DownloadTask task, int blockIndex, long currentBlockOffset, @NonNull SpeedCalculator blockSpeed) {

                    }

                    @Override
                    public void progress(@NonNull DownloadTask task, long currentOffset, @NonNull SpeedCalculator taskSpeed) {
                        int progress = (int) ((currentOffset * 1.0 / totalLength) * 100);
                        view.onDownLoadProgress(progress);
                    }

                    @Override
                    public void blockEnd(@NonNull DownloadTask task, int blockIndex, BlockInfo info, @NonNull SpeedCalculator blockSpeed) {

                    }

                    @Override
                    public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull SpeedCalculator taskSpeed) {
                        if (cause == EndCause.COMPLETED) {
                            view.onDownLoadSuccess(task.getFile());
                        } else {
                            view.onDownLoadFailed(realCause.getMessage());
                        }
                    }
                });
    }
}
