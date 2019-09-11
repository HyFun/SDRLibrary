package com.sdr.lib.ui.viewbigimage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.sdr.lib.R;
import com.sdr.lib.SDR_LIBRARY;
import com.sdr.lib.base.BaseActivity;
import com.sdr.lib.util.AlertUtil;
import com.sdr.lib.util.HttpUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import io.reactivex.functions.Consumer;

public class ViewBigImageActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

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


    private boolean isLocal = true;  // 是否是本地图片
    private int position;  // 图片的位置
    private List imageList;  // 图片列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdr_activity_view_big_image);
        initIntent();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        viewPager = findViewById(R.id.sdr_view_big_image_viewpager);
        tvPageNo = findViewById(R.id.sdr_view_big_image_tv_pager);
        tvSaveImage = findViewById(R.id.sdr_view_big_image_tv_save);
    }


    private void initIntent() {
        Intent intent = getIntent();
        isLocal = intent.getBooleanExtra("isLocal", true);
        position = intent.getIntExtra("position", 0);
        imageList = (List) intent.getSerializableExtra("imageList");
        if (imageList == null || imageList.isEmpty()) {
            AlertUtil.showNegativeToastTop("图片集合不能为空");
            finish();
        }
    }

    private void initData() {
        tvSaveImage.setVisibility(isLocal ? View.GONE : View.VISIBLE);

        ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(imageList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        if (position < imageList.size() && position >= 0) {
            viewPager.setCurrentItem(position);
            onPageSelected(position);
        }
    }

    private void initListener() {
        viewPager.addOnPageChangeListener(this);
        tvSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!HttpUtil.isNetworkConnected()) {
                    AlertUtil.showNegativeToastTop("当前网络不可用，请检查你的网络设置");
                    return;
                }
                // 授权
                new RxPermissions(getActivity())
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    AlertUtil.showNormalToastTop("开始下载图片");
                                    RxSaveImage.saveImageToGallery(getContext(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), imageList.get(position));
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected int onHeaderBarToolbarRes() {
        return 0;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.sdr_activity_up_out);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageSelected(int position) {
        tvPageNo.setText((position + 1) + "/" + imageList.size());
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        finish();
    }


    private class ImagePagerAdapter extends PagerAdapter {
        private List images;

        public ImagePagerAdapter(List images) {
            this.images = images;
        }

        @Override
        public int getCount() {
            if (images == null)
                return 0;
            return images.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.sdr_layout_item_vp_view_big_image, container, false);
            final SubsamplingScaleImageView imageView = view.findViewById(R.id.sdr_view_big_image_item_photoview);
            final ProgressBar progressBar = view.findViewById(R.id.sdr_view_big_image_item_progress);
            progressBar.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(ViewBigImageActivity.this);
            imageView.setMinimumDpi(50);
            imageView.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
            SDR_LIBRARY.getInstance().getGlide().with(getContext())
                    .download(images.get(position))
                    .into(new SimpleTarget<File>(SimpleTarget.SIZE_ORIGINAL, SimpleTarget.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                            progressBar.setVisibility(View.GONE);
                            // 显示图片
                            imageView.setImage(ImageSource.uri(Uri.fromFile(resource)));
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            AlertUtil.showNegativeToastTop("资源加载失败");
                            progressBar.setVisibility(View.GONE);
                        }
                    });


            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 查看多张图片
     *
     * @param context
     * @param position
     * @param imageList
     */
    public static void startImageList(Context context, boolean isLocal, int position, List imageList) {
        Intent intent = new Intent(context, ViewBigImageActivity.class);
        intent.putExtra("isLocal", isLocal);
        intent.putExtra("position", position);
        intent.putExtra("imageList", (Serializable) imageList);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.sdr_activity_up_in, 0);
        ActivityCompat.startActivity(context, intent, compat.toBundle());
    }
}
