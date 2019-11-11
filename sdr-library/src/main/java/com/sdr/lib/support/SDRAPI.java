package com.sdr.lib.support;

import com.sdr.lib.support.update.UpdateInfo;
import com.sdr.lib.support.weather.Weather;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by HyFun on 2018/10/30.
 * Email:775183940@qq.com
 */

public interface SDRAPI {
    String WEATHER_URL = "https://free-api.heweather.com";
    String UPDATE_APP_URL = "https://www.pgyer.com/";

    @GET("/s6/weather/forecast")
    @Headers("Cache-Control:public,max-age=180000")
    Observable<Weather> getWeatherData(@Query("location") String location, @Query("key") String key);

    @GET("apiv2/app/check")
    Observable<UpdateInfo> checkUpdate(@Query("_api_key") String apiKey, @Query("appKey") String appKey, @Query("buildVersion") String versionName);

    @Streaming
    @GET
    Observable<ResponseBody> downLoadFile(@NonNull @Url String url);
}
