package com.sdr.sdrlib.common;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdr.sdrlib.R;

/**
 * Created by HyFun on 2019/05/16.
 * Email: 775183940@qq.com
 * Description:
 */

public class AppItemRecyclerAdapter extends BaseQuickAdapter<MainItem, BaseViewHolder>{

    public AppItemRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainItem item) {
        TextView textView = (TextView) helper.itemView;
        textView.setText(item.getTitle());
        textView.setOnClickListener(item.getOnClickListener());
    }

    /**
     *  设置adapter
     * @param recyclerView
     * @return
     */
    public static final AppItemRecyclerAdapter setAdapter(RecyclerView recyclerView){
        AppItemRecyclerAdapter recyclerAdapter = new AppItemRecyclerAdapter(R.layout.layout_main_recycler_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(recyclerAdapter);
        return recyclerAdapter;
    }
}
