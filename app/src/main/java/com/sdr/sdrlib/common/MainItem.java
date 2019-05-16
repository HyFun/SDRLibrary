package com.sdr.sdrlib.common;

import android.view.View;

public class MainItem {
    private String title;
    private View.OnClickListener onClickListener;

    public MainItem(String title, View.OnClickListener onClickListener) {
        this.title = title;
        this.onClickListener = onClickListener;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}