package com.sdr.sdrlib;

import android.content.Intent;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.sdr.lib.rx.RxUtil;
import com.sdr.lib.util.PermissionUtil;
import com.sdr.sdrlib.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class SplashActivity extends BaseActivity {

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Logger.d("悬浮窗权限：" + PermissionUtil.Check.haveFloatPermission(getContext()));
        start();
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
