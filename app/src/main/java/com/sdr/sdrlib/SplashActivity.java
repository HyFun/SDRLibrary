package com.sdr.sdrlib;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sdr.sdrlib.base.BaseActivity;


public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.sdr_activity_zoom_in, R.anim.sdr_activity_zoom_out);
                finish();
            }
        }, 2000);
    }

    @Override
    protected boolean isImageHeader() {
        return true;
    }
}
