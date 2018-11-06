package com.sdr.sdrlib.base;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.sdr.sdrlib.R;

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

    @Override
    protected int onHeaderBarToolbarRes() {
        return R.layout.layout_public_toolbar_white;
    }

    @Override
    protected Drawable onHeaderBarDrawable() {
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.colorPrimary));
        return colorDrawable;
    }

    @Override
    protected int onHeaderBarStatusViewAlpha() {
        return 90;
    }
}
