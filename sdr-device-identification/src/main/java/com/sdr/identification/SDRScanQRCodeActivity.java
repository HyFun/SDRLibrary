package com.sdr.identification;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sdr.lib.base.BaseActivity;
import com.sdr.lib.util.AlertUtil;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class SDRScanQRCodeActivity extends BaseActivity {

    private View viewOperateContainer;
    private ImageView viewOperateClose;
    private ImageView viewOperateFlash;

    private boolean isLightEnable = false;  // 闪光灯 默认是关闭的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdr_activity_identify_scan_qr_code);
        initView();
        /**
         * 执行扫面Fragment的初始化操作
         */
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.sdr_layout_identify_qr_container);

        captureFragment.setAnalyzeCallback(analyzeCallback);
        /**
         * 替换我们的扫描控件
         */
        getSupportFragmentManager().beginTransaction().replace(R.id.qr_scan_frame_container, captureFragment).commit();


        initListener();
    }

    private void initView() {
        viewOperateContainer = findViewById(R.id.qr_scan_operate_container);
        viewOperateClose = findViewById(R.id.qr_scan_operate_iv_close);
        viewOperateFlash = findViewById(R.id.qr_scan_operate_iv_flash);
        viewOperateContainer.setPadding(0, getStatusBarHeight(), 0, 0);
    }

    private void initListener() {
        viewOperateClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavigationOnClickListener();
            }
        });
        viewOperateFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLightEnable = !isLightEnable;
                CodeUtils.isLightEnable(isLightEnable);
                viewOperateFlash.setImageResource(isLightEnable ? R.drawable.qrcode_ic_flash_on_black_24dp : R.drawable.qrcode_ic_flash_off_black_24dp);
            }
        });


    }


    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            setResult(RESULT_OK, resultIntent);
            finish();
        }

        @Override
        public void onAnalyzeFailed() {
            AlertUtil.showNegativeToastTop("二维码/条形码识别失败");
        }
    };


    // ————————————————————————系统回调————————————————————————————————


    @Override
    protected void setNavigationOnClickListener() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        setNavigationOnClickListener();
    }

    @Override
    protected int onHeaderBarToolbarRes() {
        return 0;
    }
}
