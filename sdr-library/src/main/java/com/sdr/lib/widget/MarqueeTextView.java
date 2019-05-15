package com.sdr.lib.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by HyFun on 2018/12/24.
 * Email: 775183940@qq.com
 * Description:
 */

public class MarqueeTextView extends android.support.v7.widget.AppCompatTextView {
    public MarqueeTextView(Context context) {
        super(context);
        init();
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setSingleLine();
        setMarqueeRepeatLimit(-1);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
    }

    @Override
    public boolean isSelected() {
        return true;
    }
}
