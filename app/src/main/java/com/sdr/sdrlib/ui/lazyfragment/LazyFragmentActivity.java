package com.sdr.sdrlib.ui.lazyfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.sdr.lib.base.BaseSimpleFragment;
import com.sdr.sdrlib.R;
import com.sdr.sdrlib.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LazyFragmentActivity extends BaseActivity {

    @BindView(R.id.lazy_fragment_tab)
    TabLayout tab;
    @BindView(R.id.lazy_fragment_viewpager)
    ViewPager viewPager;


    String[] titles = {"第一页", "第二页", "第三页", "第四页", "第五页", "第六页"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lazy_fragment);
        setTitle("懒加载fragment");
        setDisplayHomeAsUpEnabled();

        List<BaseSimpleFragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            fragmentList.add(new LazySimpleFragment(titles[i]));
        }

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPageAdapter(getSupportFragmentManager(), titles, fragmentList);
        viewPager.setAdapter(fragmentPagerAdapter);
        tab.setupWithViewPager(viewPager);

    }

    public static class FragmentPageAdapter extends FragmentPagerAdapter {
        private String[] titles;
        private List<BaseSimpleFragment> fragmentList;

        public FragmentPageAdapter(FragmentManager fm, String[] titles, List<BaseSimpleFragment> fragmentList) {
            super(fm);
            this.titles = titles;
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
