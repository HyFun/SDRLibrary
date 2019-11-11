package com.sdr.sdrlib;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;
import com.sdr.lib.rx.RxUtil;
import com.sdr.lib.support.ActivityCollector;
import com.sdr.lib.util.PermissionUtil;
import com.sdr.sdrlib.base.BaseActivity;
import com.sdr.sdrlib.util.AppUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import rx_activity_result2.Result;
import rx_activity_result2.RxActivityResult;


public class SplashActivity extends BaseActivity {

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Logger.d("悬浮窗权限：" + PermissionUtil.Check.haveFloatPermission(getContext()));
        // 判断是否获取了权限
        if (PermissionUtil.Check.haveFloatPermission(getContext())) {
            start();
        } else {
            MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                    .title("请求权限")
                    .content("请求获取悬浮窗权限，以便更好的显示提示!")
                    .cancelable(false)
                    .positiveText("确定")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            RxActivityResult.on(getActivity())
                                    .startIntent(PermissionUtil.Navigate.requestFloatPermission())
                                    .subscribe(new Consumer<Result<AppCompatActivity>>() {
                                        @Override
                                        public void accept(Result<AppCompatActivity> appCompatActivityResult) throws Exception {
                                            if (PermissionUtil.Check.haveFloatPermission(getContext())) {
                                                dialog.dismiss();
                                                start();
                                            }
                                        }
                                    });
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            ActivityCollector.getInstance().forceExit();
                        }
                    })
                    .show();
        }
    }

    private void start() {
        disposable = Observable.timer(2500, TimeUnit.MILLISECONDS)
                .compose(RxUtil.io_main())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        overridePendingTransition(R.anim.sdr_activity_zoom_in, R.anim.sdr_activity_zoom_out);
                        finish();
                    }
                });
    }

    @Override
    protected boolean isImageHeader() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
