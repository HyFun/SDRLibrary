package com.sdr.lib.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;

/**
 * Created by HyFun on 2018/09/21.
 * Email:775183940@qq.com
 */

class HttpClientUtil {


    public static String getNetCachePath(Context context){
        return context.getCacheDir().getAbsolutePath()+ File.separator+"NetCache";
    }
}
