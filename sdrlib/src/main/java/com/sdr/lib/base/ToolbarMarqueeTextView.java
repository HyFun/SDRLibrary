package com.sdr.lib.base;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by HyFun on 2018/08/09.
 * Email:775183940@qq.com
 */
class ToolbarMarqueeTextView extends android.support.v7.widget.AppCompatTextView {
    public ToolbarMarqueeTextView(Context context) {
        super(context);
    }

    public ToolbarMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToolbarMarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
