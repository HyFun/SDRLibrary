package com.sdr.lib.support.weather;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.sdr.lib.http.HttpClient;
import com.sdr.lib.rx.RxUtils;
import com.sdr.lib.rx.WeatherException;
import com.sdr.lib.support.ACache;
import com.sdr.lib.support.SDRAPI;
import com.sdr.lib.support.path.AppPath;
import com.sdr.lib.util.CommonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by HyFun on 2018/10/29.
 * Email:775183940@qq.com
 * 获取天气的Observable
 */

public class WeatherObservable {
    private static final String WEATHER_CACHE = "WEATHER_CACHE";


    private List<String> keylist = new ArrayList<>();
    private int currentIndex = 0;
    private SDRAPI weatherAPI;
    private ACache aCache;

    private FragmentActivity activity;
    private String locationCode;  // 城市代码

    public WeatherObservable(FragmentActivity activity) {
        this.activity = activity;
        init();
    }

    public WeatherObservable(String locationCode) {
        this.locationCode = locationCode;
        init();
    }

    public Observable<Weather> getWeather() {
        String weatherJson = aCache.getAsString(WEATHER_CACHE);
        if (weatherJson == null) {
            // 没有缓存  先定位
            if (TextUtils.isEmpty(locationCode)) {
                return getAuthorWeather();
            } else {
                return getWeatherData();
            }
        } else {
            // 有缓存  直接获取本地缓存
            return getLocalWeather(weatherJson);
        }
    }

    // ————————————————————私有方法————————————————————————

    /**
     * 初始化
     */
    private void init() {
        keylist.add("720b3dbddc5c4d6391b0ae457cfede34"); // 18738079950@163.com
        keylist.add("fcd6cf466aa64d52b2577578c26aeab7"); // 2823288867@qq.com
        keylist.add("10de6ec03cea4938a7bfe4dd4ce00e94"); //1240452759@qq.com
        keylist.add("275bfd7bb4e344dfaaa27315fdd9a186"); //775183940@qq.com
        keylist.add("477210d57370499caea971e4efdcdf44"); //heyongfeng@sdesrd.com
        keylist.add("fee7141b24c647e7a918017889490307"); //18738079950@139.com
        keylist.add("e78e9e23a1c3449d95926abb2b531cd0"); //18738079950@sohu.com

        weatherAPI = HttpClient.getInstance().createRetrofit(SDRAPI.WEATHER_URL, HttpClient.getInstance().getOkHttpClient(), SDRAPI.class);
        aCache = ACache.get(new File(AppPath.getUserInfoCache()));
    }

    private Observable<Weather> getAuthorWeather() {
        return CommonUtil.getRxLocation(activity)
                .flatMap(new Function<Location, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Location location) throws Exception {
                        String longitude = location.getLongitude() + "";
                        String latitude = location.getLatitude() + "";
                        if (TextUtils.isEmpty(longitude) || TextUtils.isEmpty(latitude)) {
                            return RxUtils.createData("");
                        }
                        return RxUtils.createData(longitude + "," + latitude);
                    }
                })
                .flatMap(new Function<String, ObservableSource<Weather>>() {
                    @Override
                    public ObservableSource<Weather> apply(String s) throws Exception {
                        if (TextUtils.isEmpty(s)) {
                            locationCode = "CN101010100";
                        } else {
                            locationCode = s;
                        }
                        return getWeatherData();
                    }
                });
    }

    private Observable<Weather> getWeatherData() {
        if (currentIndex < keylist.size()) {
            String key = keylist.get(currentIndex);
            return weatherAPI.getWeatherData(locationCode, key)
                    .flatMap(new Function<Weather, ObservableSource<Weather>>() {
                        @Override
                        public ObservableSource<Weather> apply(final Weather weather) throws Exception {
                            // 判断weather 是否合法
                            String status = weather.getHeWeather6().get(0).getStatus();
                            if ("ok".equals(status)) {
                                // 说明能获取到
                                // 存储下来  存三小时
                                aCache.put(WEATHER_CACHE, HttpClient.gson.toJson(weather), ACache.TIME_HOUR * 3);
                                return RxUtils.createData(weather);
                            } else {
                                // 没有获取到
                                currentIndex++;
                                return getWeatherData();
                            }
                        }
                    });
        } else {
            // 没有
            return Observable.error(new WeatherException("没有可用的key"));
        }
    }

    private Observable<Weather> getLocalWeather(final String weatherJson) {
        return Observable.just(0)
                .flatMap(new Function<Integer, ObservableSource<Weather>>() {
                    @Override
                    public ObservableSource<Weather> apply(Integer integer) throws Exception {
                        if (weatherJson == null) {
                            return Observable.error(new WeatherException("天气获取异常"));
                        } else {
                            Weather weather = HttpClient.gson.fromJson(weatherJson, new TypeToken<Weather>() {
                            }.getType());
                            return RxUtils.createData(weather);
                        }
                    }
                });
    }
}
