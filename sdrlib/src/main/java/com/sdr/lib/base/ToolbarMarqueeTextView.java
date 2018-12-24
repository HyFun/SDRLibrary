package com.sdr.lib.base;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * Created by HyFun on 2018/08/09.
 * Email:775183940@qq.com
 */
class ToolbarMarqueeTextView extends android.support.v7.widget.AppCompatTextView {
    public ToolbarMarqueeTextView(Context context) {
        super(context);
        setFocusable(true);
    }

    public ToolbarMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
    }

    public ToolbarMarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
    }
}
