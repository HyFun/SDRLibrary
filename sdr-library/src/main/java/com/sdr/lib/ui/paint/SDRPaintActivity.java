package com.sdr.lib.ui.paint;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sdr.lib.R;
import com.sdr.lib.base.BaseActivity;
import com.sdr.lib.util.AlertUtil;
import com.sdr.lib.util.ImageUtil;
import com.sdr.lib.widget.PaintView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SDRPaintActivity extends BaseActivity {

    public static final String RESULT = "RESULT";

    private PaintView paintView;
    private ImageView viewUndo, viewRedo, viewClear;
    private Button viewSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdr_activity_paint);
        setDisplayHomeAsUpEnabled();
        paintView = findViewById(R.id.sdr_paint_view);
        viewUndo = findViewById(R.id.sdr_paint_iv_undo);
        viewRedo = findViewById(R.id.sdr_paint_iv_redo);
        viewClear = findViewById(R.id.sdr_paint_iv_clear);
        viewSave = findViewById(R.id.sdr_paint_btn_save);


        viewUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.undo();
            }
        });

        viewClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.clearAll();
            }
        });

        viewRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.redo();
            }
        });

        viewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
                File file = paintView.saveJpeg(path, name);
                if (file == null) {
                    AlertUtil.showNegativeToastTop("保存失败", "您不能保存空白的画板");
                    return;
                }
                ImageUtil.notifyAlbumDataChanged(file);
                Intent intent = new Intent();
                intent.putExtra(RESULT, file.getAbsolutePath());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    protected boolean isImageHeader() {
        return true;
    }
}
