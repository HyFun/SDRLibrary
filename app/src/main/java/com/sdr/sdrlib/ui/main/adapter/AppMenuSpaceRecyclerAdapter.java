package com.sdr.sdrlib.ui.main.adapter;

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
import com.sdr.lib.support.menu.MenuItem;
import com.sdr.lib.util.CommonUtil;
import com.sdr.lib.widget.RoundFrameLayout;
import com.sdr.sdrlib.R;

import java.util.List;

/**
 * Created by HyFun on 2019/05/06.
 * Email: 775183940@qq.com
 * Description:
 */

public class AppMenuSpaceRecyclerAdapter extends BaseQuickAdapter<MenuItem, BaseViewHolder> {

    public AppMenuSpaceRecyclerAdapter(int layoutResId, @Nullable List<MenuItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MenuItem item) {
        RoundFrameLayout frameLayout = helper.getView(R.id.sdr_public_recycler_item_menu_rf);
        ImageView imageView = helper.getView(com.sdr.lib.R.id.sdr_public_recycler_item_menu_iv_icon);
        TextView tvTitle = helper.getView(com.sdr.lib.R.id.sdr_public_recycler_item_menu_tv_title);
        TextView tvMark = helper.getView(com.sdr.lib.R.id.sdr_public_recycler_item_menu_tv_marks);
        int position = helper.getLayoutPosition();
        int radius = CommonUtil.dip2px(mContext, 10);
        frameLayout.setRadius(position == 0 ? radius : 0, position == 3 ? radius : 0, position == 4 ? radius : 0, position == 7 ? radius : 0);


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


    public static final AppMenuSpaceRecyclerAdapter setAdapter(RecyclerView recyclerView, List<MenuItem> menuItemList) {
        AppMenuSpaceRecyclerAdapter menuSpaceRecyclerAdapter = new AppMenuSpaceRecyclerAdapter(R.layout.layout_item_recycler_app_menu_space, menuItemList);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 4));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(menuSpaceRecyclerAdapter);
        return menuSpaceRecyclerAdapter;
    }
}
