package com.sdr.sdrlib.ui.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by HyFun on 2019/04/30.
 * Email: 775183940@qq.com
 * Description:
 */

public class MainTopPagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> titles;

    public MainTopPagerAdapter(Context context, List<String> titles) {
        this.context = context;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        TextView textView = new TextView(context);
        textView.setText(titles.get(position));
        textView.setGravity(Gravity.CENTER);
        container.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return textView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }
}
