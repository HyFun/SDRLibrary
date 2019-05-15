package com.sdr.lib.ui.pop;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2018/4/16.
 */

public class PopwindowMenuFrameLayout extends FrameLayout implements PopWindowMenuHelper.OnClickViewPosition {
    private int[] clickPositions = new int[2];

    public PopwindowMenuFrameLayout(Context context) {
        super(context);
    }

    public PopwindowMenuFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopwindowMenuFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            clickPositions[0] = (int) event.getRawX();
            clickPositions[1] = (int) event.getRawY();
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public int[] getClickPosition() {
        return clickPositions;
    }
}
