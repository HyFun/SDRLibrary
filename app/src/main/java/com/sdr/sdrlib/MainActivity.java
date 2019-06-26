package com.sdr.sdrlib;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.sdr.lib.http.HttpClient;
import com.sdr.lib.rx.RxObserver;
import com.sdr.lib.rx.RxUtils;
import com.sdr.lib.support.weather.Weather;
import com.sdr.lib.support.weather.WeatherObservable;
import com.sdr.lib.util.CommonUtil;
import com.sdr.sdrlib.base.BaseActivity;
import com.sdr.sdrlib.common.AppItemRecyclerAdapter;
import com.sdr.sdrlib.common.MainItem;
import com.sdr.sdrlib.http.API;
import com.sdr.sdrlib.ui.SDRDeviceIdentificationActivity;
import com.sdr.sdrlib.ui.SDRLibraryActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.ResourceObserver;
import okhttp3.ResponseBody;

public class MainActivity extends BaseActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

//        AppUtil.getWeather(this, new ResourceObserver<Weather>() {
//            @Override
//            public void onNext(Weather weather) {
//                Logger.json(HttpClient.gson.toJson(weather));
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });

        new RxPermissions(getActivity())
                .request(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .flatMap(new Function<Boolean, ObservableSource<Location>>() {
                    @Override
                    public ObservableSource<Location> apply(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            return RxUtils.createData(CommonUtil.getLocation(getContext()));
                        } else {
                            return Observable.error(new Exception("授权失败"));
                        }
                    }
                })
                //.delay(1000, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Location, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(Location location) throws Exception {
                        double la = location.getLatitude();
                        double lo = location.getLongitude();
                        Logger.d(TAG, "授权成功");
                        String loctionCode = la + "," + lo;

                        return HttpClient.getInstance().createRetrofit("https://www.baidu.com/", HttpClient.getInstance().getOkHttpClient(), API.class).getBaidu("https://www.baidu.com/");
//                        return new WeatherObservable(loctionCode)
//                                .getWeather();
                    }
                })
                .compose(RxUtils.io_main())
                .subscribe(new ResourceObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String string = responseBody.string().toString();
                            Logger.d(string);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 打印出异常信息
                        Logger.t(HttpClient.TAG).e(e, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


        AppItemRecyclerAdapter adapter = AppItemRecyclerAdapter.setAdapter(recyclerView);
        // SDR-LIBRARY
        adapter.addData(new MainItem("sdr-library", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SDRLibraryActivity.class));
            }
        }));

        // SDR-DEVICE-IDENTIFICATION
        adapter.addData(new MainItem("sdr-device-identification", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SDRDeviceIdentificationActivity.class));
            }
        }));


    }


    @Override
    protected boolean onActivityAnimate() {
        return false;
    }
}
