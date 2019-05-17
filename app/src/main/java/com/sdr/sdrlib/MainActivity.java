package com.sdr.sdrlib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sdr.sdrlib.base.BaseActivity;
import com.sdr.sdrlib.common.AppItemRecyclerAdapter;
import com.sdr.sdrlib.common.MainItem;
import com.sdr.sdrlib.ui.SDRDeviceIdentificationActivity;
import com.sdr.sdrlib.ui.SDRLibraryActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

//        String[] permissions = {
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION
//        };
//        new RxPermissions(getActivity())
//                .request(permissions)
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean aBoolean) throws Exception {
//                        Logger.e("MainActivity>>>>>>>" + aBoolean);
//                    }
//                });




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
}
