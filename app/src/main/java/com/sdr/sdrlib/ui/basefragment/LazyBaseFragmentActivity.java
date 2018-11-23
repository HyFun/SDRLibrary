package com.sdr.sdrlib.ui.basefragment;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.sdr.lib.base.BaseSimpleFragment;
import com.sdr.sdrlib.R;
import com.sdr.sdrlib.base.BaseActivity;
import com.sdr.sdrlib.ui.lazyfragment.LazyFragmentActivity;
import com.sdr.sdrlib.ui.lazyfragment.LazySimpleFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LazyBaseFragmentActivity extends BaseActivity {
    @BindView(R.id.lazy_base_fragment_viewpager)
    ViewPager viewPager;

    String[] titles = {"第一页", "第二页", "第三页", "第四页", "第五页", "第六页"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lazy_base_fragment);
        List<BaseSimpleFragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            fragmentList.add(new LazyBaseFragment(titles[i]));
        }
        FragmentPagerAdapter fragmentPagerAdapter = new LazyFragmentActivity.FragmentPageAdapter(getSupportFragmentManager(), titles, fragmentList);
        viewPager.setAdapter(fragmentPagerAdapter);
    }

    @Override
    protected int onHeaderBarToolbarRes() {
        return 0;
    }
}
