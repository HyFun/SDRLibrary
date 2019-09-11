package com.sdr.sdrlib.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.sdr.lib.support.menu.MenuItem;
import com.sdr.lib.support.weather.Weather;
import com.sdr.lib.support.weather.WeatherRecyclerAdapter;
import com.sdr.lib.support.weather.WeatherUtil;
import com.sdr.lib.util.CommonUtil;
import com.sdr.lib.widget.InnerViewPagerNestedScrollView;
import com.sdr.lib.widget.VPSwipeRefreshLayout;
import com.sdr.sdrlib.GlideApp;
import com.sdr.sdrlib.R;
import com.sdr.sdrlib.base.BaseActivity;
import com.sdr.sdrlib.ui.main.adapter.MainSecondMenuBackgroundPagerAdapter;
import com.sdr.sdrlib.ui.main.adapter.MainSecondMenuPagerAdapter;
import com.sdr.sdrlib.ui.main.adapter.MainTopPagerAdapter;
import com.sdr.sdrlib.util.AppUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.observers.ResourceObserver;
import me.relex.circleindicator.CircleIndicator;

public class MainModeTwoActivity extends BaseActivity {

    @BindView(R.id.main_two_swipe)
    VPSwipeRefreshLayout swipe;
    @BindView(R.id.main_two_scroll_view)
    InnerViewPagerNestedScrollView scrollView;


    @BindView(R.id.main_two_image_weather_bg)
    ImageView viewImageWeatherBg;
    @BindView(R.id.main_two_recycler_weather)
    RecyclerView viewRecyclerWeather;

    @BindView(R.id.main_two_top_indicator)
    MagicIndicator viewTopIndicator;
    @BindView(R.id.main_two_top_viewpager)
    ViewPager viewTopViewPager;

    @BindView(R.id.main_two_viewpager_menu)
    ViewPager viewMenuVeiwPager;
    @BindView(R.id.main_two_menu_indicator)
    CircleIndicator viewMenuIndicator;
    @BindView(R.id.main_two_viewpager_menu_2)
    ViewPager viewMenuViewPager2;
    @BindView(R.id.main_two_menu_indicator_2)
    CircleIndicator viewMenuIndicator2;


    private WeatherRecyclerAdapter weatherRecyclerAdapter;
    private String subTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mode_two);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        ((ViewGroup) viewRecyclerWeather.getParent()).setPadding(0, getHeaderBarHeight(), 0, 0);
    }

    private void initData() {
        weatherRecyclerAdapter = WeatherRecyclerAdapter.setAdapter(viewRecyclerWeather);
        // top info
        {
            List<String> titles = new ArrayList<>();
            titles.add("水质");
            titles.add("水位");
            titles.add("雨情");
            // 设置adapter
            viewTopViewPager.setAdapter(new MainTopPagerAdapter(getContext(), titles));

            CommonNavigator commonNavigator = new CommonNavigator(this);
            commonNavigator.setAdjustMode(true);
            commonNavigator.setAdapter(new CommonNavigatorAdapter() {
                @Override
                public int getCount() {
                    return titles.size();
                }

                @Override
                public IPagerTitleView getTitleView(Context context, int index) {
                    SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                    simplePagerTitleView.setText(titles.get(index));
                    simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    simplePagerTitleView.setNormalColor(Color.parseColor("#cccccc"));
                    simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.colorPrimary));
                    simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewTopViewPager.setCurrentItem(index);
                        }
                    });
                    return simplePagerTitleView;
                }

                @Override
                public IPagerIndicator getIndicator(Context context) {
                    WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                    indicator.setFillColor(getResources().getColor(R.color.colorBackground));
                    indicator.setHorizontalPadding(CommonUtil.dip2px(context, 30));
                    return indicator;
                }

                @Override
                public float getTitleWeight(Context context, int index) {
                    return 1.0f;
                }
            });
            viewTopIndicator.setNavigator(commonNavigator);
            ViewPagerHelper.bind(viewTopIndicator, viewTopViewPager);
        }

        // 菜单 1
        {
            AppMenu appMenu = new AppMenu(getActivity());
            MainSecondMenuBackgroundPagerAdapter mainSecondMenuBackgroundPagerAdapter = new MainSecondMenuBackgroundPagerAdapter(getContext(), appMenu.getSecondMenuList());
            viewMenuVeiwPager.setAdapter(mainSecondMenuBackgroundPagerAdapter);
            viewMenuIndicator.setViewPager(viewMenuVeiwPager);

            // 更新
            List<List<MenuItem>> datas = mainSecondMenuBackgroundPagerAdapter.getDatas();
            for (int i = 0; i < datas.size(); i++) {
                List<MenuItem> menuItemList = datas.get(i);
                for (int j = 0; j < menuItemList.size(); j++) {
                    MenuItem menuItem = menuItemList.get(j);
                    menuItem.setBadge("9");
                }
            }

            mainSecondMenuBackgroundPagerAdapter.notifyDataSetChanged();
        }

        // 菜单2
        {
            AppMenu appMenu = new AppMenu(getActivity());
            MainSecondMenuPagerAdapter mainSecondMenuPagerAdapter = new MainSecondMenuPagerAdapter(getContext(), appMenu.getSecondMenuList());
            viewMenuViewPager2.setAdapter(mainSecondMenuPagerAdapter);
            viewMenuIndicator2.setViewPager(viewMenuViewPager2);
            // 更新
            List<List<MenuItem>> datas = mainSecondMenuPagerAdapter.getDatas();
            for (int i = 0; i < datas.size(); i++) {
                List<MenuItem> menuItemList = datas.get(i);
                for (int j = 0; j < menuItemList.size(); j++) {
                    MenuItem menuItem = menuItemList.get(j);
                    menuItem.setBadge("9");
                }
            }

            mainSecondMenuPagerAdapter.notifyDataSetChanged();
        }


        // 获取数据
        AppUtil.getWeather(this,new ResourceObserver<Weather>() {
            @Override
            public void onNext(Weather weather) {
                subTitle = weather.getHeWeather6().get(0).getBasic().getParent_city() + "天气";
                setTitle(subTitle);
                weatherRecyclerAdapter.setNewData(weather.getHeWeather6().get(0).getDaily_forecast());
                // 背景
                int code = Integer.parseInt(weather.getHeWeather6().get(0).getDaily_forecast().get(0).getCond_code_d());
                int res = WeatherUtil.getWeatherImage(code);
                GlideApp.with(getContext())
                        .load(res)
                        .apply(RequestOptions.fitCenterTransform())
                        .into(viewImageWeatherBg);
                setHeaderImage(res);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    private void initListener() {
        //swipe.setProgressViewEndTarget(true, getHeaderBarHeight());
        swipe.setProgressViewOffset(true, 0, getHeaderBarHeight());
        setHeaderBarScrollChange(scrollView, CommonUtil.dip2px(getContext(), 240));
    }

    @Override
    public void onScrollChange(int scrollY, float alpha) {
        super.onScrollChange(scrollY, alpha);
        swipe.setEnabled(scrollY == 0);
        if (alpha >= 0.5f) {
            setTitle(getResources().getString(R.string.app_name));
        } else {
            setTitle(subTitle);
        }
    }

    @Override
    protected boolean isImageHeader() {
        return true;
    }
}
