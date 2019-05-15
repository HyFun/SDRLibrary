package com.sdr.lib.support.weather;

import com.sdr.lib.R;

/**
 * Created by HyFun on 2018/10/29.
 * Email:775183940@qq.com
 */

public class WeatherUtil {
    private WeatherUtil() {
    }


    public static int getWeatherImage(int code) {
        switch (code) {
//            晴
            case 100:
            case 103:
            case 900:
                return R.mipmap.weather_sunny;
//             云
            case 101:
            case 102:
            case 201:
            case 202:
            case 203:
            case 204:
                return R.mipmap.weather_cloudy;
//            雾
            case 500:
            case 501:
                return R.mipmap.weather_fog;
//            霾
            case 502:
                return R.mipmap.weather_haze;
//            阴
            case 104:
            case 205:
            case 206:
            case 207:
            case 208:
            case 209:
            case 210:
            case 211:
            case 212:
            case 213:
                return R.mipmap.weather_overcast;
//            雨
            case 200:
            case 300:
            case 301:
            case 305:
            case 306:
            case 307:
            case 308:
            case 309:
            case 310:
            case 311:
            case 312:
                return R.mipmap.weather_rain;
//            沙
            case 503:
            case 504:
            case 507:
            case 508:
                return R.mipmap.weather_sand;
//            雪
            case 313:
            case 400:
            case 401:
            case 402:
            case 403:
            case 404:
            case 405:
            case 406:
            case 407:
            case 901:
                return R.mipmap.weather_snow;
//            雷
            case 302:
            case 304:
            case 303:
                return R.mipmap.weather_thunder;
//            na
            case 999:
                return R.mipmap.weather_na;
        }
        return R.mipmap.weather_na;
    }
}
