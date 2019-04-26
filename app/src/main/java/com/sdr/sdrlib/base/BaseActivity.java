package com.sdr.sdrlib.base;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by HyFun on 2018/11/06.
 * Email:775183940@qq.com
 */

public class BaseActivity extends com.sdr.lib.base.BaseActivity {

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }
}
