package com.sdr.sdrlib.util;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.sdr.lib.rx.RxUtil;
import com.sdr.lib.support.path.AppPath;
import com.sdr.lib.support.update.AppNeedUpdateListener;
import com.sdr.lib.support.update.UpdateAppManager;
import com.sdr.lib.support.weather.Weather;
import com.sdr.lib.support.weather.WeatherObservable;
import com.sdr.lib.util.AlertUtil;
import com.sdr.lib.util.CommonUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.reactivex.functions.Consumer;
import io.reactivex.observers.ResourceObserver;

/**
 * Created by HyFun on 2018/11/06.
 * Email:775183940@qq.com
 */

public class AppUtil {
    private AppUtil() {
    }

    public static void getWeather(FragmentActivity activity, ResourceObserver<Weather> weatherResourceObserver) {
        new RxPermissions(activity)
                .request(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Location location = CommonUtil.getLocation(activity);
                            double la = location.getLatitude();
                            double lo = location.getLongitude();
                            String loctionCode = la + "," + lo;
                            new WeatherObservable(loctionCode)
                                    .getWeather()
                                    .compose(RxUtil.io_main())
                                    .subscribeWith(weatherResourceObserver);
                        } else {
                            AlertUtil.showNegativeToastTop("授权失败", "请在设置中开启定位权限");
                        }
                    }
                });
    }


    public static void getWeather(Fragment fragment, ResourceObserver<Weather> weatherResourceObserver) {
        new RxPermissions(fragment)
                .request(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Location location = CommonUtil.getLocation(fragment.getContext());
                            double la = location.getLatitude();
                            double lo = location.getLongitude();
                            String loctionCode = la + "," + lo;
                            new WeatherObservable(loctionCode)
                                    .getWeather()
                                    .compose(RxUtil.io_main())
                                    .subscribeWith(weatherResourceObserver);
                        } else {
                            AlertUtil.showNegativeToastTop("授权失败", "请在设置中开启定位权限");
                        }
                    }
                });
    }
}
