package com.sdr.sdrlib.common.menu;

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
import com.sdr.sdrlib.R;

import java.util.List;

/**
 * Created by Administrator on 2018/4/23.
 */

public class MenuRecyclerAdapter extends BaseQuickAdapter<MenuItem, BaseViewHolder> {

    public MenuRecyclerAdapter(int layoutResId, @Nullable List<MenuItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MenuItem item) {
        ImageView imageView = helper.getView(R.id.main_home_top_menu_recycler_item_iv);
        TextView textView = helper.getView(R.id.main_home_top_menu_recycler_item_tv_title);
        TextView badge = helper.getView(R.id.main_home_top_menu_recycler_item_tv_badge);

        // 菜单图标
        Glide.with(mContext).load(item.getImageRes()).into(imageView);
        imageView.setColorFilter(item.getImgColor(), PorterDuff.Mode.SRC_IN);
        // 菜单标题
        textView.setText(item.getTitle());
        textView.setTextColor(item.getTitleColor());

        // 菜单角标
        String badgeStr = item.getBadge();
        if (TextUtils.isEmpty(badgeStr) || "0".equals(badgeStr)) {
            badge.setVisibility(View.GONE);
        } else {
            badge.setText(badgeStr);
            badge.setVisibility(View.VISIBLE);
            // 显示动画
            setViewZoomInAnim(badge, 800);
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


    public static final MenuRecyclerAdapter setAdapter(RecyclerView recyclerView, List<MenuItem> menuItemList) {
        MenuRecyclerAdapter mSecondMenuAdapter = new MenuRecyclerAdapter(R.layout.layout_public_item_menu, menuItemList);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 4));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mSecondMenuAdapter);
        return mSecondMenuAdapter;
    }
}
