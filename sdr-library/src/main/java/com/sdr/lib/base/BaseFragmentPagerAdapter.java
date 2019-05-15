package com.sdr.lib.base;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by HyFun on 2018/12/18.
 * Email: 775183940@qq.com
 * Description:
 */

public class BaseFragmentPagerAdapter<T extends BaseSimpleFragment> extends FragmentPagerAdapter {
    protected List<T> datas;

    public BaseFragmentPagerAdapter(FragmentManager fm, List<T> datas) {
        super(fm);
        this.datas = datas;
    }

    @Override
    public Fragment getItem(int position) {
        return datas.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return datas.get(position).getFragmentTitle();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    public List<T> getDatas() {
        return datas;
    }
}
