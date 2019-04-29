package com.sdr.sdrlib.ui.lazyfragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sdr.lib.http.HttpClient;
import com.sdr.lib.rx.RxUtils;
import com.sdr.lib.support.weather.Weather;
import com.sdr.lib.support.weather.WeatherObservable;
import com.sdr.lib.util.AlertUtil;
import com.sdr.sdrlib.R;
import com.sdr.sdrlib.base.BaseFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.ResourceObserver;

/**
 * Created by HyFun on 2018/11/06.
 * Email:775183940@qq.com
 */

@SuppressLint("ValidFragment")
public class LazySimpleFragment extends BaseFragment {
    private String title;

    public LazySimpleFragment(String title) {
        this.title = title;
    }

    @BindView(R.id.lazy_fragment_swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.lazy_fragment_textview)
    TextView textView;
    @BindView(R.id.lazy_fragment_button)
    Button button;

    @Nullable
    @Override
    public View onCreateFragmentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lazy_fragment, container, false);
        return view;
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        // 加载数据
        swipe.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
                textView.setText(title);
            }
        }, 1000);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RxPermissions(getActivity())
                        .request(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    new WeatherObservable(getActivity()).getWeather()
                                            .compose(RxUtils.io_main())
                                            .subscribeWith(new ResourceObserver<Weather>() {
                                                @Override
                                                public void onNext(Weather weather) {
                                                    AlertUtil.showDialog(getActivity(), "获取天气成功", HttpClient.gson.toJson(weather));
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    Logger.e(e, e.getMessage());
                                                }

                                                @Override
                                                public void onComplete() {

                                                }
                                            });
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
                textView.setText(title);
            }
        }, 1000);
    }

    @Override
    public String getFragmentTitle() {
        return title;
    }
}
