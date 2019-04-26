package com.sdr.sdrlib.ui.lazyfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdr.sdrlib.R;
import com.sdr.sdrlib.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by HyFun on 2018/11/06.
 * Email:775183940@qq.com
 */

@SuppressLint("ValidFragment")
public class LazySimpleFragment extends BaseFragment {
    private String title;

    public LazySimpleFragment(String title) {
        this.title = title;
    }

    @BindView(R.id.lazy_fragment_swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.lazy_fragment_textview)
    TextView textView;

    @Nullable
    @Override
    public View onCreateFragmentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lazy_fragment, container, false);
        return view;
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        // 加载数据
        swipe.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
                textView.setText(title);
            }
        }, 1000);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
                textView.setText(title);
            }
        }, 1000);
    }

    @Override
    public String getFragmentTitle() {
        return title;
    }
}
