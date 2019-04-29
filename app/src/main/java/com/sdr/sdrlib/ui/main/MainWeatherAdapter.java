package com.sdr.sdrlib.ui.main;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdr.lib.support.weather.Weather;
import com.sdr.sdrlib.R;
import com.sdr.sdrlib.app.Constant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/10.
 */

public class MainWeatherAdapter extends BaseQuickAdapter<Weather.HeWeather6Bean.DailyForecastBean, BaseViewHolder> {

    public MainWeatherAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Weather.HeWeather6Bean.DailyForecastBean item) {
        TextView day = helper.getView(R.id.tv_item_weather_date);
        final ImageView imageView = helper.getView(R.id.img_item_weather);
        TextView tem = helper.getView(R.id.tv_item_weather_tem);
        TextView status = helper.getView(R.id.tv_item_weather_status);
        day.setText(helper.getLayoutPosition() == 0 ? "今天" : formatDate(item.getDate()));
        imageView.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Glide.with(mContext).load("https://cdn.heweather.com/cond_icon/" + item.getCond_code_d() + ".png").into(imageView);
        tem.setText(item.getTmp_min() + "°C~" + item.getTmp_max() + "°C");
        status.setText(item.getCond_txt_d());
    }


    public static final MainWeatherAdapter  setAdapter(RecyclerView recyclerView){
        MainWeatherAdapter mainWeatherAdapter = new MainWeatherAdapter(R.layout.layout_item_recycler_main_weather);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mainWeatherAdapter);
        return mainWeatherAdapter;
    }


    private static String formatDate(String dateStr) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("MM-dd").format(date) + " " + Constant.Date.weeks[date.getDay()];
    }
}
