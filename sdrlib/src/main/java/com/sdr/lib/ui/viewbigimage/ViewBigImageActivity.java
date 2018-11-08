package com.sdr.lib.ui.viewbigimage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.sdr.lib.R;
import com.sdr.lib.base.BaseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewBigImageActivity extends BaseActivity {

    /**
     * 保存图片
     */
    private TextView tvSaveImage;
    /**
     * 用于管理图片的滑动
     */
    private ViewPager viewPager;
    /**
     * 显示当前图片的页数
     */
    private TextView tvPageNo;


    private int position;
    private List<Object> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_big_image);
        initView();
        initIntent();

    }

    private void initView() {
        viewPager = findViewById(R.id.sdr_view_big_image_viewpager);
        tvPageNo = findViewById(R.id.sdr_view_big_image_tv_pager);
        tvSaveImage = findViewById(R.id.sdr_view_big_image_tv_save);
    }


    private void initIntent() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        imageList = (List<Object>) intent.getSerializableExtra("imageList");
    }

    @Override
    protected int onHeaderBarToolbarRes() {
        return 0;
    }


    /**
     * 查看多张图片
     *
     * @param context
     * @param position
     * @param imageList
     */
    public static void startImageList(Context context, int position, List<Object> imageList) {
        Intent intent = new Intent(context, ViewBigImageActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("imageList", (Serializable) imageList);
        context.startActivity(intent);
    }


    /**
     * 查看一张图片
     *
     * @param context
     * @param image
     */
    public static void startImage(Context context, Object image) {
        List<Object> imageList = new ArrayList<>();
        imageList.add(image);
        startImageList(context, 0, imageList);
    }
}
