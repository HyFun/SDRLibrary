package com.sdr.lib.base;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sdr.lib.R;
import com.sdr.lib.widget.MarqueeTextView;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;

/**
 * Created by HyFun on 2018/11/22.
 * Email: 775183940@qq.com
 * Description:
 */

public abstract class BaseFragment extends BaseSimpleFragment implements OnScrollListener {
    private View loadingView;
    private View realContentView;

    private FrameLayout headerView;
    private ImageView headerImage;

    protected Toolbar toolBar;


    private Drawable navigationIconDrawable;
    private int slidingDistance;
    private AnimationDrawable mAnimationDrawable;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = onCreateFragmentView(inflater, container, savedInstanceState);
        bindButterKnife(view);

        View root = LayoutInflater.from(getContext()).inflate(R.layout.sdr_layout_base_activity, null, false);
        FrameLayout rootView = root.findViewById(R.id.sdr_base_activity_root);
        FrameLayout contentView = root.findViewById(R.id.sdr_base_activity_content);
        loadingView = LayoutInflater.from(getContext()).inflate(R.layout.sdr_layout_public_loading_view, null, false);
        realContentView = view;
        if (onHeaderBarToolbarRes() == 0) {

        } else {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.sdr_layout_base_header_view, null, false);
            headerView = v.findViewById(R.id.sdr_base_activity_header_view);
            headerImage = v.findViewById(R.id.sdr_base_activity_header_image);
            toolBar = (Toolbar) LayoutInflater.from(getContext()).inflate(onHeaderBarToolbarRes(), headerView, false);
            headerView.addView(toolBar);
            rootView.addView(headerView);

            navigationIconDrawable = toolBar.getNavigationIcon();
            toolBar.setBackgroundColor(Color.TRANSPARENT);
            toolBar.setNavigationIcon(null);
            headerImage.getLayoutParams().height = getHeaderBarHeight();
            setHeaderImage(onHeaderBarImage());

            // toolbar的类型
            if (isImageHeader()) {
                setHeaderBarAlpha(0);
            } else {
                // 设置margin
                int headerHeight = getHeaderBarHeight();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
                params.setMargins(0, headerHeight, 0, 0);
                contentView.setLayoutParams(params);
                setHeaderBarAlpha(255);
            }
            // 设置toolbar的margin
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) toolBar.getLayoutParams();
            layoutParams.setMargins(0, Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? getStatusBarHeight() : 0, 0, 0);
        }
        // 添加 content view  和  loading view
        contentView.addView(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        contentView.addView(loadingView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        ImageView loadingImage = loadingView.findViewById(R.id.sdr_base_activity_iv_loading);
        // 加载动画
        mAnimationDrawable = (AnimationDrawable) loadingImage.getDrawable();
        showContentView();

        return rootView;
    }


    @Nullable
    public abstract View onCreateFragmentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected void bindButterKnife(View view) {

    }


    @Override
    public void onScrollChange(int scrollY) {
        if (slidingDistance <= 0) return;
        if (scrollY < 0) {
            scrollY = 0;
        }
        float alpha = (float) scrollY / (float) slidingDistance;
        if (scrollY <= slidingDistance) {
            setHeaderBarAlpha((int) (255 * alpha));
        } else {
            setHeaderBarAlpha(255);
            alpha = 1;
        }
        onScrollChange(scrollY, alpha);
    }

    /**
     * 暴露出的滚动监听
     *
     * @param scrollY
     * @param alpha
     */
    public void onScrollChange(int scrollY, float alpha) {

    }

    public final void showLoadingView() {
        if (loadingView != null && loadingView.getVisibility() != View.VISIBLE) {
            loadingView.setVisibility(View.VISIBLE);
        }
        // 开始动画
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        if (realContentView.getVisibility() != View.GONE) {
            realContentView.setVisibility(View.GONE);
        }
    }

    public final void showContentView() {
        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (realContentView.getVisibility() != View.VISIBLE) {
            realContentView.setVisibility(View.VISIBLE);
        }
    }

    // ————————————————————————————————————————————————————————
    // -----------------------------------------------配置方法--------------------------------------------

    /**
     * 默认的toolbar
     *
     * @return
     */
    protected int onHeaderBarToolbarRes() {
        return 0;
    }

    /**
     * 配置状态栏透明度
     *
     * @return
     * @deprecated 该方法废除 无用
     */
    @IntRange(from = 0, to = 255)
    @Deprecated
    protected int onHeaderBarStatusViewAlpha() {
        return 0;
    }

    /**
     * 设置HeaderbarView的颜色 drawable
     *
     * @return
     */
    protected Drawable onHeaderBarDrawable() {
        return new ColorDrawable(Color.parseColor("#333333"));
    }

    /**
     * 配置headerbar默认显示的图片
     * 如果配置正确 那么久显示该图片
     *
     * @return
     */
    protected Object onHeaderBarImage() {
        return "";
    }

    /**
     * 是否是HeaderImage
     *
     * @return
     */
    protected boolean isImageHeader() {
        return false;
    }

    /**
     * header bar 标题文字的位置
     *
     * @return
     */
    protected int onHeaderBarTitleGravity() {
        return Gravity.LEFT;
    }

    // -----------------------------------------------设置方法--------------------------------------------

    /**
     * 设置headerbar加载的图片
     *
     * @param image
     */
    protected final void setHeaderImage(Object image) {
        if (headerImage == null) return;
        // 高斯模糊背景 原来 参数：12,5  23,4
        RequestOptions options = new RequestOptions();
        options.transforms(new BlurTransformation(23, 4), new CropTransformation(0, headerImage.getLayoutParams().height, CropTransformation.CropType.BOTTOM));
        options.error(onHeaderBarDrawable()).placeholder(onHeaderBarDrawable());
        Glide.with(this)
                .load(image)
                .apply(options)
                .into(headerImage);
    }

    /**
     * 设置状态栏的深浅
     *
     * @param alpha 0~255
     */
    protected final void setHeaderBarAlpha(@IntRange(from = 0, to = 255) int alpha) {
        if (headerImage == null) return;
        headerImage.setAlpha((float) alpha / 255f);
    }

    /**
     * 设置滚动监听事件  使HeaderBar随滚动而改变alpha
     *
     * @param view
     * @param viewHeight
     */
    protected final void setHeaderBarScrollChange(OnScrollListenerView view, int viewHeight) {
        // 首先判断是否是imageHeader
        if (!isImageHeader())
            return;
        // 其次判断viewHeight的高度是否大于headerbar的高度
        slidingDistance = viewHeight - getHeaderBarHeight();
        if (slidingDistance <= 0)
            return;
        // 最后设置事件
        view.setOnScrollListener(this);
        onScrollChange(0);
    }

    protected void setDisplayHomeAsUpEnabled() {
        if (toolBar == null) return;
        setDisplayHomeAsUpEnabled(toolBar, navigationIconDrawable);
    }

    protected void setDisplayHomeAsUpEnabled(Toolbar toolBar, Drawable drawable) {
        if (toolBar == null) return;
        setDisplayHomeAsUpEnabled(toolBar, drawable, -2);
    }

    protected void setDisplayHomeAsUpEnabled(Toolbar toolBar, Drawable drawable, int color) {
        if (toolBar == null) return;
        if (color != -2) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
        toolBar.setNavigationIcon(drawable);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNavigationOnClickListener();
            }
        });
    }

    protected void setNavigationOnClickListener() {
        getActivity().finish();
    }

    /**
     * 重新设置toolbar的高度 并设置它的padding
     *
     * @param toolBar
     */
    protected final void setToolbarPadding(Toolbar toolBar) {
        if (toolBar == null) return;
        toolBar.getLayoutParams().height = getHeaderBarHeight();
        toolBar.setPadding(0, Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? getStatusBarHeight() : 0, 0, 0);
    }

    /**
     * 设置tool bar标题
     */
    public void setTitle(int titleId) {
        setTitle(getResources().getString(titleId));
    }

    public void setTitle(CharSequence title) {
        setTitle(toolBar, title);
    }

    public void setTitle(Toolbar toolbar, CharSequence title) {
        setTitle(toolbar, title, onHeaderBarTitleGravity());
    }

    public void setTitle(Toolbar toolbar, CharSequence title, int gravity) {
        if (toolbar == null) return;
        toolbar.setTitle("");
        toolbar.setContentInsetStartWithNavigation(0);
        // 添加自定义的textview 首先寻找
        MarqueeTextView textView = getHeaderBarTitleView(toolbar);
        if (textView == null) {
            textView = new MarqueeTextView(getContext());
            textView.setText(title);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
            params.gravity = gravity;
            toolbar.addView(textView, params);
        } else {
            textView.setText(title);
        }
    }

    /**
     * 设置title的透明度
     *
     * @param alpha
     */
    protected void setHeaderBarTitleViewAlpha(float alpha) {
        for (int i = 0; i < toolBar.getChildCount(); i++) {
            if (toolBar.getChildAt(i) instanceof AppCompatTextView) {
                AppCompatTextView textView = (AppCompatTextView) toolBar.getChildAt(i);
                textView.setAlpha(alpha);
            }
        }
    }

    // -----------------------------------------------获取方法--------------------------------------------

    /**
     * 获取HeaderBar的高度
     *
     * @return 状态栏高度
     */
    public final int getHeaderBarHeight() {
        // 获得状态栏高度
        int toolBarHeight = 0;
        if (toolBar != null) {
            toolBarHeight = toolBar.getLayoutParams().height;
        } else {
            toolBarHeight = Util.dip2px(getContext(), 56);
        }
        return toolBarHeight + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? getStatusBarHeight() : 0);
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    public final int getStatusBarHeight() {
        // 获得状态栏高度
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }


    /**
     * 获取HeaderBarView
     *
     * @return
     */
    public final FrameLayout getHeaderBarView() {
        return headerView;
    }

    /**
     * 获取toolbar 的titleview
     *
     * @param toolbar
     * @return
     */
    public final MarqueeTextView getHeaderBarTitleView(Toolbar toolbar) {
        MarqueeTextView textView = null;
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            if (toolbar.getChildAt(i) instanceof MarqueeTextView) {
                textView = (MarqueeTextView) toolbar.getChildAt(i);
                break;
            }
        }
        return textView;
    }


}
