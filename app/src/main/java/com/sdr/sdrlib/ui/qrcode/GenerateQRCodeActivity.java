package com.sdr.sdrlib.ui.qrcode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sdr.identification.RxSDRDeviceIdentification;
import com.sdr.sdrlib.R;
import com.sdr.sdrlib.base.BaseActivity;

import butterknife.BindView;

public class GenerateQRCodeActivity extends BaseActivity {

    @BindView(R.id.generate_qr_code_edt)
    EditText edtContent;
    @BindView(R.id.generate_qr_code_btn)
    Button button;
    @BindView(R.id.generate_qr_code_image)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qrcode);
        setTitle("生成二维码");
        setDisplayHomeAsUpEnabled();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtContent.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                Bitmap bitmap = RxSDRDeviceIdentification.createQRImage(content, 600);
                imageView.setImageBitmap(bitmap);
            }
        });
    }
}
