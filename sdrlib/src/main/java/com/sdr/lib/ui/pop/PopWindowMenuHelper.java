package com.sdr.lib.ui.pop;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.sdr.lib.R;
import com.sdr.lib.util.CommonUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */

public class PopWindowMenuHelper {

    private PopWindowMenuHelper() {
    }

    public interface OnItemClickListener {
        void onClickPopwindowMenuItem(View view, PopupWindowMenu menuItem, PopupWindow popupWindow, int position);
    }

    // 获取点击屏幕位置的接口
    public interface OnClickViewPosition {
        int[] getClickPosition();
    }

    private static abstract class Builder<T> {
        private List<PopupWindowMenu> mMenuList;
        private Activity mActivity;
        private RecyclerView.Adapter mRecyclerAdapter;

        private LayoutInflater mLayoutInflater;

        private T t;

        public Builder(Activity activity, List<PopupWindowMenu> list) {
            mActivity = activity;
            mMenuList = list;

            init();
        }

        public Builder(Activity activity, RecyclerView.Adapter adapter) {
            mActivity = activity;
            mRecyclerAdapter = adapter;

            init();
        }


        private View popContentView;
        // 四个三角形
        private ImageView leftTopTriangle;
        private ImageView rightTopTriangle;
        private ImageView leftBottomTriangle;
        private ImageView rightBottomTriangle;

        private FrameLayout cardView;
        private RecyclerView recyclerView;

        private PopupWindow popupWindow;

        private void init() {
            mLayoutInflater = LayoutInflater.from(mActivity);
            t = getBuilderInstance();
            // 加载布局
            popContentView = mLayoutInflater.inflate(R.layout.sdr_layout_public_popwindow, null);
            // 四个三角形
            leftTopTriangle = (ImageView) popContentView.findViewById(R.id.hyf_popwindow_triangle_left_top);
            rightTopTriangle = (ImageView) popContentView.findViewById(R.id.hyf_popwindow_triangle_right_top);
            leftBottomTriangle = (ImageView) popContentView.findViewById(R.id.hyf_popwindow_triangle_left_bottom);
            rightBottomTriangle = (ImageView) popContentView.findViewById(R.id.hyf_popwindow_triangle_right_bottom);

            cardView = (FrameLayout) popContentView.findViewById(R.id.hyf_popwindow_list_container);
            recyclerView = (RecyclerView) popContentView.findViewById(R.id.hyf_popwindow_recyclerview);
            popupWindow = createPopupWindow(popContentView);

            popupWindow.setFocusable(true);
            popupWindow.setClippingEnabled(false);

            // adapter
            if (mRecyclerAdapter == null && mMenuList != null) {
                mRecyclerAdapter = new PopwindowRecyclerAdapter(mActivity, mMenuList, popupWindow);
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            recyclerView.setAdapter(mRecyclerAdapter);
        }

        /**
         * ——————————————————设置公共属性的方法START————————————————————
         */

        // 是否显示黑色透明背景
        private boolean isBlur;

        public T setIsBlur(boolean isBlur) {
            this.isBlur = isBlur;
            return t;
        }

        /**
         * ——————————————————设置公共属性的方法END————————————————————
         */


        public final void show() {
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // 显示和关闭事件
            if (isBlur) {
                CommonUtil.setTransformBg(0.6f, mActivity);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        CommonUtil.setTransformBg(1.0f, mActivity);
                    }
                });
            }
            // 设置弹出位置
            showAnimation2AtLocation(popupWindow);
        }

        /**
         * —————————————————————一些设置方法———————————————————
         */

        // 根据方位显示三角形
        protected final void setTriangleVisiabled(int direction) {
            switch (direction) {
                case Util.LEFT_TOP:
                    rightBottomTriangle.setVisibility(View.VISIBLE);
                    break;
                case Util.RIGHT_TOP:
                    leftBottomTriangle.setVisibility(View.VISIBLE);
                    break;
                case Util.LEFT_BOTTOM:
                    rightTopTriangle.setVisibility(View.VISIBLE);
                    break;
                case Util.RIGHT_BOTTOM:
                    leftTopTriangle.setVisibility(View.VISIBLE);
                    break;
            }
        }


        /**
         * —————————————————————获取的一些方法———————————————————
         */
        protected final PopupWindow getPopupWindow() {
            return popupWindow;
        }

        protected final FrameLayout getRecyclerContainer() {
            return cardView;
        }

        protected final Activity getActivity() {
            return mActivity;
        }

        /**
         * ——————————————————需要实现的抽象方法——————————————————————
         */

        abstract T getBuilderInstance();

        abstract PopupWindow createPopupWindow(View view);

        // 设置popupwindow的显示动画和显示位置
        abstract void showAnimation2AtLocation(PopupWindow popupWindow);

    }

    /**
     * ————————————————————————————————————————————————————————————
     */
    public static class TopBuilder extends Builder<TopBuilder> {
        private View view;

        public TopBuilder(Activity activity, List<PopupWindowMenu> list, View view) {
            super(activity, list);
            this.view = view;
        }

        public TopBuilder(Activity activity, RecyclerView.Adapter adapter, View view) {
            super(activity, adapter);
            this.view = view;
        }


        // 设置是否显示三角形
        private boolean isShowTriangle = false;

        public TopBuilder setShowTriangle(boolean showTriangle) {
            isShowTriangle = showTriangle;
            return this;
        }

        @Override
        TopBuilder getBuilderInstance() {
            return this;
        }

        @Override
        PopupWindow createPopupWindow(View view) {
            return new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        @Override
        void showAnimation2AtLocation(PopupWindow popupWindow) {
            int[] positions = null;
            if (isShowTriangle) {
                positions = Util.calculatePopWindowPosByView(view, popupWindow.getContentView());
                // 设置显示三角形
                setTriangleVisiabled(positions[2]);
            } else {
                positions = Util.calculatePopWindowPosByView(view, popupWindow.getContentView());
            }
            int X = positions[0];
            int Y = positions[1];
            int direction = positions[2];
            // 设置显示动画
            Util.setShowPopupAnimation(popupWindow, direction);
            // 设置三角形显示
            if (isShowTriangle) {
                int offsetX = CommonUtil.dip2px(getActivity(), 17.5f);
                int offsetY = CommonUtil.dip2px(getActivity(), 7.5f);
                switch (direction) {
                    case Util.LEFT_TOP:
                    case Util.LEFT_BOTTOM:
                        X += offsetX;
                        break;
                    case Util.RIGHT_TOP:
                    case Util.RIGHT_BOTTOM:
                        X -= offsetX;
                        break;
                }
                Y -= offsetY;

                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(Color.WHITE);
                drawable.setCornerRadius(CommonUtil.dip2px(getActivity(), 5));
                getRecyclerContainer().setBackgroundDrawable(drawable);
            } else {
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    popupWindow.setElevation(CommonUtil.dip2px(getActivity(), 5));
                }
            }
            popupWindow.showAtLocation(view, Gravity.TOP | Gravity.START, X, Y);
        }
    }


    public static class BottomBuilder extends Builder<BottomBuilder> {

        public BottomBuilder(Activity activity, List<PopupWindowMenu> list) {
            super(activity, list);
        }

        public BottomBuilder(Activity activity, RecyclerView.Adapter adapter) {
            super(activity, adapter);
        }

        @Override
        BottomBuilder getBuilderInstance() {
            return this;
        }

        @Override
        PopupWindow createPopupWindow(View view) {
            return new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        @Override
        void showAnimation2AtLocation(PopupWindow popupWindow) {
            popupWindow.setAnimationStyle(R.style.Popwindow_Animation_Bottom);
            popupWindow.showAtLocation(getActivity().findViewById(Window.ID_ANDROID_CONTENT), Gravity.BOTTOM, 0, checkDeviceHasNavigationBar(getActivity()) ? getNavigationBarHeight(getActivity()) : 0);
        }

        // 获取底部导航栏高度
        private int getNavigationBarHeight(Context context) {
            int height = 0;
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            height = resources.getDimensionPixelSize(resourceId);
            return height;
        }

        //获取是否存在NavigationBar
        private boolean checkDeviceHasNavigationBar(Context context) {
            boolean hasNavigationBar = false;
            Resources rs = context.getResources();
            int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
            if (id > 0) {
                hasNavigationBar = rs.getBoolean(id);
            }
            try {
                Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
                Method m = systemPropertiesClass.getMethod("get", String.class);
                String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
                if ("1".equals(navBarOverride)) {
                    hasNavigationBar = false;
                } else if ("0".equals(navBarOverride)) {
                    hasNavigationBar = true;
                }
            } catch (Exception e) {

            }
            return hasNavigationBar;

        }
    }

    public static class MiddleBuilder extends Builder<MiddleBuilder> {
        private OnClickViewPosition mOnClickViewPosition;

        public MiddleBuilder(Activity activity, List<PopupWindowMenu> list, OnClickViewPosition onClickViewPosition) {
            super(activity, list);
            mOnClickViewPosition = onClickViewPosition;
        }

        public MiddleBuilder(Activity activity, RecyclerView.Adapter adapter, OnClickViewPosition onClickViewPosition) {
            super(activity, adapter);
            mOnClickViewPosition = onClickViewPosition;
        }

        // 设置是否显示三角形
        private boolean isShowTriangle = false;

        public MiddleBuilder setShowTriangle(boolean showTriangle) {
            isShowTriangle = showTriangle;
            return this;
        }

        @Override
        MiddleBuilder getBuilderInstance() {
            return this;
        }

        @Override
        PopupWindow createPopupWindow(View view) {
            return new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        @Override
        void showAnimation2AtLocation(PopupWindow popupWindow) {
            int[] clickPositions = mOnClickViewPosition.getClickPosition();
            int clickRawX = clickPositions[0];
            int clickRawY = clickPositions[1];
            int[] locations = null;
            if (isShowTriangle) {
                locations = Util.calculatePopWindowPosByPoint(clickRawX, clickRawY, popupWindow.getContentView());
                // 设置显示三角形
                setTriangleVisiabled(locations[2]);
            } else {
                locations = Util.calculatePopWindowPosByPoint(clickRawX, clickRawY, popupWindow.getContentView());
            }
            int X = locations[0];
            int Y = locations[1];
            int direction = locations[2];
            // 设置显示动画
            Util.setShowPopupAnimation(popupWindow, direction);
            // 是否显示三角形
            if (isShowTriangle) {
                int offsetX = CommonUtil.dip2px(getActivity(), 17.5f);
                int offsetY = CommonUtil.dip2px(getActivity(), 7.5f);
                // X轴相加
                switch (direction) {
                    case Util.LEFT_TOP:
                    case Util.LEFT_BOTTOM:
                        X += offsetX;
                        break;
                    case Util.RIGHT_TOP:
                    case Util.RIGHT_BOTTOM:
                        X -= offsetX;
                        break;
                }
                Y -= offsetY;

                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(Color.WHITE);
                drawable.setCornerRadius(CommonUtil.dip2px(getActivity(), 5));
                getRecyclerContainer().setBackgroundDrawable(drawable);
            } else {
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    popupWindow.setElevation(CommonUtil.dip2px(getActivity(), 5));
                }
            }

            popupWindow.showAtLocation(getActivity().findViewById(Window.ID_ANDROID_CONTENT), Gravity.TOP | Gravity.START, X, Y);
        }
    }

    // 获取ActionMenuView
    public static final ActionMenuItemView getToolbarActionMenuItemView(Toolbar toolbar, int itemId) {
        ActionMenuItemView itemView = null;
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof ActionMenuView) {
                ActionMenuView actionMenuView = (ActionMenuView) view;
                for (int j = 0; j < actionMenuView.getChildCount(); j++) {
                    ActionMenuItemView item = (ActionMenuItemView) actionMenuView.getChildAt(j);
                    if (item.getId() == itemId) {
                        itemView = item;
                        break;
                    }
                }
                break;
            }
        }

        return itemView;
    }
}
