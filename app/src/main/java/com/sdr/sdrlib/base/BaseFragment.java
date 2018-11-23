package com.sdr.sdrlib.base;

import android.graphics.drawable.Drawable;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by HyFun on 2018/11/23.
 * Email: 775183940@qq.com
 * Description:
 */

public abstract class BaseFragment extends com.sdr.lib.base.BaseFragment {
    @Override
    protected void bindButterKnife(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected int onHeaderBarToolbarRes() {
        return BaseConfig.onHeaderBarRes();
    }

    @Override
    protected Drawable onHeaderBarDrawable() {
        return BaseConfig.onHeaderBarDrawable(getContext());
    }

    @Override
    protected int onHeaderBarTitleGravity() {
        return BaseConfig.onHeaderBarGravity();
    }
}
