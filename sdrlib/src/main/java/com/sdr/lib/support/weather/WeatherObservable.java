package com.sdr.lib.support.weather;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.sdr.lib.http.HttpClient;
import com.sdr.lib.rx.RxUtils;
import com.sdr.lib.rx.WeatherException;
import com.sdr.lib.support.ACache;
import com.sdr.lib.support.SDRAPI;
import com.sdr.lib.support.path.AppPath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
        return Observable.just(0)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        try {
                            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                            String bestProvider = locationManager.getBestProvider(getProvider(), true);
                            Location location = locationManager.getLastKnownLocation(bestProvider);
                            if (location == null) {
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            }
                            String longitude = location.getLongitude() + "";
                            String latitude = location.getLatitude() + "";
                            return RxUtils.createData(longitude + "," + latitude);
                        } catch (Exception e) {
                            return RxUtils.createData("");
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
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
                            Weather weather = HttpClient.gson.fromJson(weatherJson,new TypeToken<Weather>(){}.getType());
                            return RxUtils.createData(weather);
                        }
                    }
                });
    }

    /**
     * 定位查询条件
     * 返回查询条件 ，获取目前设备状态下，最适合的定位方式
     */
    private Criteria getProvider() {
        // 构建位置查询条件
        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        //Criteria.ACCURACY_FINE,当使用该值时，在建筑物当中，可能定位不了,建议在对定位要求并不是很高的时候用Criteria.ACCURACY_COARSE，避免定位失败
        // 查询精度：高
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 是否查询海拨：否
        criteria.setAltitudeRequired(false);
        // 是否查询方位角 : 否
        criteria.setBearingRequired(false);
        // 是否允许付费：是
        criteria.setCostAllowed(false);
        // 电量要求：低
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // 返回最合适的符合条件的provider，第2个参数为true说明 , 如果只有一个provider是有效的,则返回当前provider
        return criteria;
    }
}
