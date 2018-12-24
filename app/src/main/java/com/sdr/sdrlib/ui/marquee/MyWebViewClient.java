package com.sdr.sdrlib.ui.marquee;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orhanobut.logger.Logger;

/**
 * Created by HyFun on 2018/10/15.
 * Email:775183940@qq.com
 */

public class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Logger.d("加载的URL：\n" + url);
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }
}
