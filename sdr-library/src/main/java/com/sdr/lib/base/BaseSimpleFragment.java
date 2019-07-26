package com.sdr.lib.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sdr.lib.R;

/**
 * Created by HyFun on 2018/11/06.
 * Email:775183940@qq.com
 */

abstract class BaseSimpleFragment extends AbstractFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Override
    public void onRefresh() {

    }

    /**
     * 获取title
     *
     * @return
     */
    public String getFragmentTitle() {
        return "";
    }



    /**
     * 获取空数据的view
     */

    protected View getEmptyView() {
        return getEmptyView(null);
    }

    protected View getEmptyView(String message) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sdr_layout_public_empty_view, null);
        if (!TextUtils.isEmpty(message)) {
            TextView textView = view.findViewById(R.id.text);
            textView.setText(message);
        }
        return view;
    }
}
