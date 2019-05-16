package com.sdr.sdrlib.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sdr.sdrlib.R;
import com.sdr.sdrlib.base.BaseActivity;

/**
 * Created by HyFun on 2019/05/16.
 * Email: 775183940@qq.com
 * Description:
 */

public class SDRLibraryActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("SDRLibraryActivity");
    }
}
