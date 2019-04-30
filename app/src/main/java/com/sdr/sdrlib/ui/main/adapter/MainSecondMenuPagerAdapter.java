package com.sdr.sdrlib.ui.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.sdr.sdrlib.common.menu.MenuItem;
import com.sdr.sdrlib.common.menu.MenuRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HyFun on 2018/08/23.
 * Email:775183940@qq.com
 */

public class MainSecondMenuPagerAdapter extends PagerAdapter {
    private Context context;
    private List<List<MenuItem>> datas;
    private List<MenuRecyclerAdapter> recyclerAdapterList = new ArrayList<>();

    public MainSecondMenuPagerAdapter(Context context, List<List<MenuItem>> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        RecyclerView recyclerView = new RecyclerView(context);
        MenuRecyclerAdapter menuRecyclerAdapter = MenuRecyclerAdapter.setAdapter(recyclerView, datas.get(position));
        recyclerAdapterList.add(menuRecyclerAdapter);
        container.addView(recyclerView);
        return recyclerView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }

    @Override
    public void notifyDataSetChanged() {
        //super.notifyDataSetChanged();
        for (MenuRecyclerAdapter adapter : recyclerAdapterList) {
            adapter.notifyDataSetChanged();
        }
    }

    public List<List<MenuItem>> getDatas() {
        return datas;
    }
}
