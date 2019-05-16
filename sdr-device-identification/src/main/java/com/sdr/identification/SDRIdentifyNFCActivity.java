package com.sdr.identification;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sdr.lib.base.BaseActivity;
import com.sdr.lib.rx.RxUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class SDRIdentifyNFCActivity extends BaseActivity {
    private static final int 等待扫描中 = 0;
    private static final int 设备不支持 = 1;
    private static final int 扫描失败 = 2;
    private static final int 扫描成功 = 3;

    private TextView tvTitle, tvDes, tvResult;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdr_activity_identify_nfc);
        setDisplayHomeAsUpEnabled();
        setTitle("NFC扫描");
        initView();

        //获取NfcAdapter实例
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //获取通知
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        switchStatus(等待扫描中, null);
        if (nfcAdapter == null) {
            switchStatus(设备不支持, null);
            return;
        }
        if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
            new MaterialDialog.Builder(getContext())
                    .title("提示")
                    .content("请先打开NFC功能后重试")
                    .positiveText("确定")
                    .positiveColorRes(R.color.colorNegative)
                    .cancelable(false)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            setNavigationOnClickListener();
                        }
                    })
                    .show();
            return;
        }
        //因为启动模式是singleTop，于是会调用onNewIntent方法
        onNewIntent(getIntent());
    }

    private void initView() {
        tvTitle = findViewById(R.id.identify_nfc_tv_title);
        tvDes = findViewById(R.id.identify_nfc_tv_des);
        tvResult = findViewById(R.id.identify_nfc_tv_result);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        resolveIntent(intent);
    }


    // ——————————————————————————私有方法————————————————————————————

    private void resolveIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            processTag(intent);
        }
    }

    public void processTag(Intent intent) {
        //处理tag
        //获取到卡对象
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //获取卡id这里即uid
        byte[] aa = tagFromIntent.getId();
        String str = ByteArrayToHexString(aa);
        str = flipHexStr(str);
        switchStatus(扫描成功, str);
        Observable.just(str)
                .delay(500, TimeUnit.MILLISECONDS)
                .compose(RxUtils.<String>io_main())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Intent intent = new Intent();
                        intent.putExtra("NFC", s);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
    }

    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F"};
        String out = "";
        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    private String flipHexStr(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i <= s.length() - 2; i = i + 2) {
            result.append(new StringBuilder(s.substring(i, i + 2)));
        }
        return result.toString();
    }

    /**
     * 切换状态
     *
     * @param status
     */
    private void switchStatus(int status, String code) {
        switch (status) {
            case 等待扫描中:
                tvTitle.setText("等待扫描中");
                tvTitle.setTextColor(getResources().getColor(R.color.colorBlack));
                tvDes.setText("请将设备靠近NFC标识点进行扫描");
                break;
            case 设备不支持:
                tvTitle.setText("设备不支持NFC");
                tvTitle.setTextColor(getResources().getColor(R.color.colorNegative));
                tvDes.setText("检测到您的设备暂不支持NFC功能，请更换设备");
                break;
            case 扫描失败:
                tvTitle.setText("识别失败");
                tvTitle.setTextColor(getResources().getColor(R.color.colorNegative));
                tvDes.setText("由于未知原因NFC扫描失败");
                break;
            case 扫描成功:
                tvTitle.setText("识别成功");
                tvTitle.setTextColor(getResources().getColor(R.color.colorPositive));
                tvDes.setText("已成功识别NFC的标识，如下所示");
                tvResult.setVisibility(View.VISIBLE);
                tvResult.setText(code);
                break;
        }
    }


    // ——————————————————————————系统方法————————————————————————————

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null)
            //设置程序不优先处理
            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null)
            //设置程序优先处理
            nfcAdapter.enableForegroundDispatch(this, pendingIntent,
                    null, null);
    }

    @Override
    public void onBackPressed() {
        setNavigationOnClickListener();
    }

    @Override
    protected void setNavigationOnClickListener() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected boolean isImageHeader() {
        return true;
    }
}
