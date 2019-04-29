package com.sdr.sdrlib.ui.main;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sdr.lib.rx.RxUtils;
import com.sdr.lib.support.weather.Weather;
import com.sdr.lib.support.weather.WeatherObservable;
import com.sdr.lib.support.weather.WeatherUtil;
import com.sdr.lib.util.CommonUtil;
import com.sdr.lib.widget.InnerViewPagerNestedScrollView;
import com.sdr.sdrlib.R;
import com.sdr.sdrlib.base.BaseActivity;

import butterknife.BindView;
import io.reactivex.observers.ResourceObserver;

public class MainModeOneActivity extends BaseActivity {

    @BindView(R.id.main_one_scroll_view)
    InnerViewPagerNestedScrollView scrollView;
    // 天气
    @BindView(R.id.main_one_tv_weather_title)
    TextView tvWeatherTitle;
    @BindView(R.id.main_one_image_weather_bg)
    ImageView viewWeatherBg;
    @BindView(R.id.main_one_recycler_weather)
    RecyclerView viewRecyclerWeather;
    private MainWeatherAdapter mainWeatherAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mode_one);
        // 背景
        {
            GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor("#D79AEF"), Color.parseColor("#5279FF")});
            scrollView.setBackgroundDrawable(drawable);
            scrollView.setPadding(0, getStatusBarHeight(), 0, 0);
        }
        // 获取天气数据
        initData();
        initListener();
    }

    private void initData() {
        setTitle(getResources().getString(R.string.app_name));

        mainWeatherAdapter = MainWeatherAdapter.setAdapter(viewRecyclerWeather);


        // 获取数据
        new WeatherObservable(getActivity()).getWeather()
                .compose(RxUtils.io_main())
                .subscribeWith(new ResourceObserver<Weather>() {
                    @Override
                    public void onNext(Weather weather) {
                        tvWeatherTitle.setText(weather.getHeWeather6().get(0).getBasic().getParent_city() + "天气");
                        mainWeatherAdapter.setNewData(weather.getHeWeather6().get(0).getDaily_forecast());
                        // 背景
                        int code = Integer.parseInt(weather.getHeWeather6().get(0).getDaily_forecast().get(0).getCond_code_d());
                        int res = WeatherUtil.getWeatherImage(code);
                        Glide.with(getContext())
                                .load(res)
                                .apply(RequestOptions.fitCenterTransform())
                                .into(viewWeatherBg);
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
        setHeaderBarScrollChange(scrollView, CommonUtil.dip2px(getContext(), 240));
    }

    @Override
    public void onScrollChange(int scrollY, float alpha) {
        super.onScrollChange(scrollY, alpha);
        setHeaderBarTitleViewAlpha(alpha);
    }

    @Override
    protected boolean isImageHeader() {
        return true;
    }
}
