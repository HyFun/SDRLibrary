package com.sdr.sdrlib.util;

import android.Manifest;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.sdr.lib.rx.RxUtils;
import com.sdr.lib.support.path.AppPath;
import com.sdr.lib.support.update.AppNeedUpdateListener;
import com.sdr.lib.support.update.UpdateAppManager;
import com.sdr.lib.support.weather.Weather;
import com.sdr.lib.support.weather.WeatherObservable;
import com.sdr.lib.util.AlertUtil;
import com.sdr.lib.util.CommonUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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
                                    .compose(RxUtils.io_main())
                                    .subscribeWith(weatherResourceObserver);
                        } else {
                            AlertUtil.showNegativeToastTop("请在设置中开启定位权限");
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
                                    .compose(RxUtils.io_main())
                                    .subscribeWith(weatherResourceObserver);
                        } else {
                            AlertUtil.showNegativeToastTop("请在设置中开启定位权限");
                        }
                    }
                });
    }


    /**
     * 检测app更新
     *
     * @param activity
     * @param showDialog
     * @param needUpdateListener
     */
    public static void checkUpdate(FragmentActivity activity, boolean showDialog, AppNeedUpdateListener needUpdateListener) {
        //if (BuildConfig.DEBUG) return;
        new RxPermissions(activity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        UpdateAppManager.checkUpdate(activity, "bpm", 1, AppPath.getFilePath(), showDialog, needUpdateListener);
                    } else {

                    }
                });
    }
}
