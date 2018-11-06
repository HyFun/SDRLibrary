package com.sdr.lib.support.weather;

import com.sdr.lib.http.HttpClient;
import com.sdr.lib.rx.WeatherException;
import com.sdr.lib.support.SDRAPI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by HyFun on 2018/10/29.
 * Email:775183940@qq.com
 * 获取天气的Observable
 */

public class WeatherObservable {

    private static final List<String> keylist = new ArrayList<>();

    static {
        keylist.clear();
        keylist.add("720b3dbddc5c4d6391b0ae457cfede34"); // 18738079950@163.com
        keylist.add("fcd6cf466aa64d52b2577578c26aeab7"); // 2823288867@qq.com
        keylist.add("10de6ec03cea4938a7bfe4dd4ce00e94"); //1240452759@qq.com
        keylist.add("275bfd7bb4e344dfaaa27315fdd9a186"); //775183940@qq.com
        keylist.add("477210d57370499caea971e4efdcdf44"); //heyongfeng@sdesrd.com
        keylist.add("fee7141b24c647e7a918017889490307"); //18738079950@139.com
        keylist.add("e78e9e23a1c3449d95926abb2b531cd0"); //18738079950@sohu.com
    }


    private int currentIndex = 0;
    private String locationCode;  // 城市代码
    private SDRAPI weatherAPI;

    public WeatherObservable(String locationCode) {
        this.locationCode = locationCode;
        weatherAPI = HttpClient.getInstance().createRetrofit(SDRAPI.WEATHER_URL,
                HttpClient.getInstance().getOkHttpClient(), SDRAPI.class);
    }


    public Observable<Weather> getWeatherData() {
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
                                return Observable.create(new ObservableOnSubscribe<Weather>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<Weather> emitter) throws Exception {
                                        emitter.onNext(weather);
                                        emitter.onComplete();
                                    }
                                });
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
}
