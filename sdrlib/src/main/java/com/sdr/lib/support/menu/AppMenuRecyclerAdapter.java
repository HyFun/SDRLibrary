package com.sdr.lib.support.menu;

import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdr.lib.R;

import java.util.List;

/**
 * Created by HyFun on 2019/05/05.
 * Email: 775183940@qq.com
 * Description:
 */

public class AppMenuRecyclerAdapter extends BaseQuickAdapter<MenuItem, BaseViewHolder> {

    public AppMenuRecyclerAdapter(int layoutResId, @Nullable List<MenuItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MenuItem item) {
        ImageView imageView = helper.getView(R.id.sdr_public_recycler_item_menu_iv_icon);
        TextView tvTitle = helper.getView(R.id.sdr_public_recycler_item_menu_tv_title);
        TextView tvMark = helper.getView(R.id.sdr_public_recycler_item_menu_tv_marks);

        // 菜单图标
        Glide.with(mContext).load(item.getImageRes()).into(imageView);
        imageView.setColorFilter(item.getImgColor(), PorterDuff.Mode.SRC_IN);
        // 菜单标题
        tvTitle.setText(item.getTitle());
        tvTitle.setTextColor(item.getTitleColor());

        // 菜单角标
        String badgeStr = item.getBadge();
        if (TextUtils.isEmpty(badgeStr) || "0".equals(badgeStr)) {
            tvMark.setVisibility(View.GONE);
        } else {
            tvMark.setText(badgeStr);
            tvMark.setVisibility(View.VISIBLE);
            // 显示动画
            setViewZoomInAnim(tvMark, 800);
        }
        // 点击事件
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getOnClickMenuItemListener() != null) {
                    item.getOnClickMenuItemListener().onClick(v, item);
                }
            }
        });

    }


    /**
     * 首页由小变大的动画
     *
     * @param view
     * @param duration
     */
    private final void setViewZoomInAnim(View view, long duration) {
        view.clearAnimation();
        view.setVisibility(View.VISIBLE);
        AnimationSet set = new AnimationSet(false);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(duration);
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0,
                1,
                0,
                1,
                ScaleAnimation.RELATIVE_TO_SELF,
                0.5f,
                ScaleAnimation.RELATIVE_TO_SELF,
                0.5f
        );
        scaleAnimation.setDuration(duration);

        set.addAnimation(alphaAnimation);
        set.addAnimation(scaleAnimation);

        view.setAnimation(set);
    }


    public static final AppMenuRecyclerAdapter setAdapter(RecyclerView recyclerView, List<MenuItem> menuItemList) {
        AppMenuRecyclerAdapter mSecondMenuAdapter = new AppMenuRecyclerAdapter(R.layout.sdr_layout_public_recycler_item_app_menu, menuItemList);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 4));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mSecondMenuAdapter);
        return mSecondMenuAdapter;
    }
}
