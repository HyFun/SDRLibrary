package com.sdr.lib.support.weather;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdr.lib.R;
import com.sdr.lib.util.DateUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/10.
 */

public class WeatherRecyclerAdapter extends BaseQuickAdapter<Weather.HeWeather6Bean.DailyForecastBean, BaseViewHolder> {

    public WeatherRecyclerAdapter(int layoutResId) {
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

    /**
     * 设置adapter
     *
     * @param recyclerView
     * @return
     */
    public static final WeatherRecyclerAdapter setAdapter(RecyclerView recyclerView) {
        WeatherRecyclerAdapter weatherRecyclerAdapter = new WeatherRecyclerAdapter(R.layout.sdr_layout_public_recycler_item_weather);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(weatherRecyclerAdapter);
        return weatherRecyclerAdapter;
    }


    private static String formatDate(String dateStr) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("MM-dd").format(date) + " " + DateUtil.WEEKS[date.getDay()];
    }
}
