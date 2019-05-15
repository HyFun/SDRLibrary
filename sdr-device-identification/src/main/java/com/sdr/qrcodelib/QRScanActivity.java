package com.sdr.qrcodelib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class QRScanActivity extends AppCompatActivity {

    private View viewOperateContainer;
    private ImageView viewOperateClose;
    private ImageView viewOperateFlash;

    private boolean isLightEnable = false;  // 闪光灯 默认是关闭的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qrscan);
        initView();
        /**
         * 执行扫面Fragment的初始化操作
         */
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.qrcoce_layout_qr_container);

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

        setFullScreen(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            viewOperateContainer.setPadding(0, getStatusBarHeight(this), 0, 0);
        }
    }

    private void initListener() {
        viewOperateClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
//            bundle.putString(CodeUtils.RESULT_STRING, "");
//            resultIntent.putExtras(bundle);
//            setResult(RESULT_OK, resultIntent);
//            finish();
            Toast.makeText(QRScanActivity.this, "扫描二维码错误", Toast.LENGTH_SHORT).show();
        }
    };


    /**
     * 设置内容全屏,即内容延伸至状态栏底部,状态栏文字还在
     *
     * @param activity
     */
    private static void setFullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置透明状态栏,这样才能让 ContentView 向上
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度px
     */
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }


    /**
     * activity开启
     *
     * @param activity
     * @param requestCode
     */
    public static final void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, QRScanActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 从fragment开启
     *
     * @param fragment
     * @param requestCode
     */
    public static final void start(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), QRScanActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }
}
