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



    /**
     * 检查是否有悬浮窗权限
     *
     * @param context
     * @return
     */
    public static boolean checkFloatPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                Class cls = Class.forName("android.content.Context");
                Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls);
                if (!(obj instanceof String)) {
                    return false;
                }
                String str2 = (String) obj;
                obj = cls.getMethod("getSystemService", String.class).invoke(context, str2);
                cls = Class.forName("android.app.AppOpsManager");
                Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                declaredField2.setAccessible(true);
                Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());
                return result == declaredField2.getInt(cls);
            } catch (Exception e) {
                return false;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                if (appOpsMgr == null)
                    return false;
                int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
                        .getPackageName());
                return mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
            } else {
                return Settings.canDrawOverlays(context);
            }
        }
    }
}
