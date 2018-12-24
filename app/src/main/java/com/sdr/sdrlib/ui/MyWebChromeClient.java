package com.sdr.sdrlib.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by HyFun on 2018/10/15.
 * Email:775183940@qq.com
 */

public class MyWebChromeClient extends WebChromeClient {
    public static final int FILECHOOSER_RESULTCODE = 1;
    public static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
    public static final String FILE_TYPE = "*/*";

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;


    private Activity activity;
    private ViewWeb viewWeb;

    public MyWebChromeClient(Activity activity, ViewWeb viewWeb) {
        this.activity = activity;
        this.viewWeb = viewWeb;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (!TextUtils.isEmpty(title))
            viewWeb.changeTitle(title);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        viewWeb.progress(newProgress);
    }

    //扩展浏览器上传文件
    //3.0++版本
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        openFileChooserImpl(uploadMsg);
    }

    //3.0--版本
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooserImpl(uploadMsg);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooserImpl(uploadMsg);
    }

    // For Android > 5.0
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
        openFileChooserImplForAndroid5(uploadMsg);
        return true;
    }

    //————————————————————公开方法——————————————————————————

    /**
     * 5.0以下 上传图片成功后的回调
     */
    public void mUploadMessage(Intent intent, int resultCode) {
        if (null == mUploadMessage)
            return;
        Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;
    }

    /**
     * 5.0以上 上传图片成功后的回调
     */
    public void mUploadMessageForAndroid5(Intent intent, int resultCode) {
        if (null == mUploadMessageForAndroid5)
            return;
        Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
        if (result != null) {
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
        } else {
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
        }
        mUploadMessageForAndroid5 = null;
    }

    //————————————————————私有方法——————————————————————————

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType(FILE_TYPE);
        activity.startActivityForResult(Intent.createChooser(i, "文件选择"), FILECHOOSER_RESULTCODE);
    }

    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType(FILE_TYPE);

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "文件选择");

        activity.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }
}
