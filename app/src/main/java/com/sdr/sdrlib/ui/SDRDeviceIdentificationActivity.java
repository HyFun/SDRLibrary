package com.sdr.sdrlib.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sdr.identification.RxSDRDeviceIdentification;
import com.sdr.lib.util.AlertUtil;
import com.sdr.sdrlib.R;
import com.sdr.sdrlib.base.BaseActivity;
import com.sdr.sdrlib.common.AppItemRecyclerAdapter;
import com.sdr.sdrlib.common.MainItem;

import butterknife.BindView;
import io.reactivex.functions.Consumer;
import rx_activity_result2.Result;

/**
 * Created by HyFun on 2019/05/16.
 * Email: 775183940@qq.com
 * Description:
 */

public class SDRDeviceIdentificationActivity extends BaseActivity {
    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("SDRDeviceIdentificationActivity");

        AppItemRecyclerAdapter adapter = AppItemRecyclerAdapter.setAdapter(recyclerView);

        adapter.addData(new MainItem("扫描二维码", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxSDRDeviceIdentification.scan(getActivity())
                        .subscribe(new Consumer<Result<FragmentActivity>>() {
                            @Override
                            public void accept(Result<FragmentActivity> fragmentActivityResult) throws Exception {
                                String code = RxSDRDeviceIdentification.Helper.getScanResult(fragmentActivityResult);
                                if (code != null) {
                                    AlertUtil.showPositiveToast(code);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                AlertUtil.showNegativeToastTop(throwable.getMessage());
                            }
                        });
            }
        }));

        adapter.addData(new MainItem("NFC扫描", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxSDRDeviceIdentification
                        .nfc(getActivity())
                        .subscribe(new Consumer<Result<FragmentActivity>>() {
                            @Override
                            public void accept(Result<FragmentActivity> fragmentActivityResult) throws Exception {
                                String code = RxSDRDeviceIdentification.Helper.getNfcResult(fragmentActivityResult);
                                if (code != null) {
                                    AlertUtil.showPositiveToast(code);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                AlertUtil.showNegativeToastTop(throwable.getMessage());
                            }
                        });
            }
        }));


        adapter.addData(new MainItem("蓝牙地址", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxSDRDeviceIdentification.bluetooth(getContext());
            }
        }));


    }
}
