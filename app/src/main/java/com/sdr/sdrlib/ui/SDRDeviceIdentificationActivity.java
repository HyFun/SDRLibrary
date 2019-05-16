package com.sdr.sdrlib.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sdr.sdrlib.R;
import com.sdr.sdrlib.base.BaseActivity;
import com.sdr.sdrlib.common.AppItemRecyclerAdapter;
import com.sdr.sdrlib.common.MainItem;

import butterknife.BindView;

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

            }
        }));
    }
}
