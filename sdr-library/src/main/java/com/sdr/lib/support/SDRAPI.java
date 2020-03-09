package com.sdr.lib.support;

import com.sdr.lib.support.update.UpdateInfo;
import com.sdr.lib.support.weather.Weather;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by HyFun on 2018/10/30.
 * Email:775183940@qq.com
 */

public interface SDRAPI {
    String WEATHER_URL = "https://free-api.heweather.com";
    //    String UPDATE_APP_URL = "http://58.240.174.254:8070";
    String UPDATE_APP_URL = "https://www.pgyer.com/apiv2/";

    @GET("/s6/weather/forecast")
    @Headers("Cache-Control:public,max-age=180000")
    Observable<Weather> getWeatherData(@Query("location") String location, @Query("key") String key);

    @Headers({
            "Accept:application/json",
            "Cache-Control:public,max-age=0"
    })
//    @GET("/app/appVersion/getAppVersion")
//    Observable<UpdateInfo> checkUpdate(@Query("appName") String appName, @Query("versionCode") int versionCode);

    @FormUrlEncoded
    @POST("app/check")
    Observable<ResponseBody> checkUpdate(@Field("_api_key") String api_key, @Field("appKey") String app_key, @Field("buildVersion") String versionName);


    @Streaming
    @GET
    Observable<ResponseBody> downLoadFile(@NonNull @Url String url);
}
