package com.sdr.lib.ui.pop;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

import com.sdr.lib.R;

/**
 * Created by Administrator on 2018/4/14 0014.
 */

class Util {
    private Util() {
    }

    public static final int LEFT_TOP = 0;
    public static final int RIGHT_TOP = 1;
    public static final int LEFT_BOTTOM = 3;
    public static final int RIGHT_BOTTOM = 4;


    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView  呼出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    public static int[] calculatePopWindowPosByView(final View anchorView, final View contentView) {
        final int windowPos[] = new int[3];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        final int anchorWidth = anchorView.getWidth();
        // 获取屏幕的高宽
        final int screenHeight = getScreenHeight(anchorView.getContext());
        final int screenWidth = getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        boolean isNeedShowLeft = (screenWidth - anchorLoc[0] - anchorWidth < windowWidth);
        if (isNeedShowUp) {
            if (isNeedShowLeft) {
                // 左上角
                windowPos[0] = anchorLoc[0] - windowWidth + anchorWidth / 2;
                windowPos[1] = anchorLoc[1] - windowHeight;
                windowPos[2] = LEFT_TOP;
            } else {
                // 右上角
                windowPos[0] = anchorLoc[0] + anchorWidth / 2;
                windowPos[1] = anchorLoc[1] - windowHeight;
                windowPos[2] = RIGHT_TOP;
            }
        } else {
            if (isNeedShowLeft) {
                // 左下角
                windowPos[0] = anchorLoc[0] - windowWidth + anchorWidth / 2;
                windowPos[1] = anchorLoc[1] + anchorHeight;
                windowPos[2] = LEFT_BOTTOM;
            } else {
                // 右下角
                windowPos[0] = anchorLoc[0] + anchorWidth / 2;
                windowPos[1] = anchorLoc[1] + anchorHeight;
                windowPos[2] = RIGHT_BOTTOM;
            }
        }
        return windowPos;
    }

    /**
     * 根据点击的坐标计算出popwindow显示的位置
     */

    public static int[] calculatePopWindowPosByPoint(int X, int Y, View popContentView) {
        int[] locations = new int[3];
        popContentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupwindowWidth = popContentView.getMeasuredWidth();
        int popupwindowHeight = popContentView.getMeasuredHeight();


        boolean isNeedShowLeft = ((getScreenWidth(popContentView.getContext()) - X) < popupwindowWidth);
        boolean isNeedShowUp = ((getScreenHeight(popContentView.getContext()) - Y) < popupwindowHeight);
        if (isNeedShowLeft) {
            if (isNeedShowUp) {
                // 显示在点击的左上角
                locations[0] = X - popupwindowWidth;
                locations[1] = Y - popupwindowHeight;
                locations[2] = LEFT_TOP;
            } else {
                // 左下角
                locations[0] = X - popupwindowWidth;
                locations[1] = Y;
                locations[2] = LEFT_BOTTOM;
            }
        } else {
            if (isNeedShowUp) {
                // 右上角
                locations[0] = X;
                locations[1] = Y - popupwindowHeight;
                locations[2] = RIGHT_TOP;
            } else {
                // 右下角
                locations[0] = X;
                locations[1] = Y;
                locations[2] = RIGHT_BOTTOM;
            }
        }


        return locations;
    }


    /**
     * 获取屏幕高度(px)
     */
    private static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度(px)
     */
    private static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

//—————————————————————————初始备份—————————————————————————
//    /**
//     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
//     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
//     *
//     * @param anchorView  呼出window的view
//     * @param contentView window的内容布局
//     * @return window显示的左上角的xOff, yOff坐标
//     */
//    public static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
//        final int windowPos[] = new int[2];
//        final int anchorLoc[] = new int[2];
//        // 获取锚点View在屏幕上的左上角坐标位置
//        anchorView.getLocationOnScreen(anchorLoc);
//        final int anchorHeight = anchorView.getHeight();
//        final int anchorWidth = anchorView.getWidth();
//        // 获取屏幕的高宽
//        final int screenHeight = getScreenHeight(anchorView.getContext());
//        final int screenWidth = getScreenWidth(anchorView.getContext());
//        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        // 计算contentView的高宽
//        final int windowHeight = contentView.getMeasuredHeight();
//        final int windowWidth = contentView.getMeasuredWidth();
//        // 判断需要向上弹出还是向下弹出显示
//        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
//        boolean isNeedShowLeft = (screenWidth - anchorLoc[0] - anchorWidth < windowWidth);
//        if (isNeedShowUp) {
//            if (isNeedShowLeft) {
//                // 左上角
//            } else {
//                // 右上角
//            }
//            windowPos[0] = screenWidth - windowWidth;
//            windowPos[1] = anchorLoc[1] - windowHeight;
//        } else {
//            if (isNeedShowLeft) {
//                // 左下角
//            } else {
//                // 右下角
//            }
//            windowPos[0] = screenWidth - windowWidth;
//            windowPos[1] = anchorLoc[1] + anchorHeight;
//        }
//        return windowPos;
//    }


    /**
     * 设置popwindow 的 显示样式动画
     *
     * @param popupWindow
     * @param direction
     */
    public static final void setShowPopupAnimation(PopupWindow popupWindow, int direction) {
        switch (direction) {
            case Util.LEFT_TOP:
                popupWindow.setAnimationStyle(R.style.Popwindow_Animation_RightBottom);
                break;
            case Util.RIGHT_TOP:
                popupWindow.setAnimationStyle(R.style.Popwindow_Animation_LeftBottom);
                break;
            case Util.LEFT_BOTTOM:
                popupWindow.setAnimationStyle(R.style.Popwindow_Animation_RightTop);
                break;
            case Util.RIGHT_BOTTOM:
                popupWindow.setAnimationStyle(R.style.Popwindow_Animation_LeftTop);
                break;
        }
    }
}
