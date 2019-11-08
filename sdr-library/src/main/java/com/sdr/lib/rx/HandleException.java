package com.sdr.lib.rx;

import android.graphics.Color;
import android.graphics.ColorFilter;

import com.google.gson.JsonParseException;
import com.sdr.lib.util.HttpUtil;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

/**
 * Created by HyFun on 2018/11/01.
 * Email:775183940@qq.com
 */

public abstract class HandleException {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;


    private Throwable throwable;

    public HandleException(Throwable throwable) {
        this.throwable = throwable;
        handleException();
    }

    public boolean parseException(Throwable throwable) {
        return false;
    }

    public abstract void commonException(Exception exception);

    private void handleException() {
        // CommonException ce = new CommonException(throwable);
        Exception ce = null;
        if (!HttpUtil.isNetworkConnected()) {
            // 说明没网
            ce = new Exception("网络不给力，请检查网络设置", throwable);
            commonException(ce);
        } else if (throwable instanceof HttpException) {
            //HTTP错误
            HttpException httpException = (HttpException) throwable;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ce = new Exception("网络连接错误", throwable);
                    commonException(ce);
                    break;
            }
        } else if (throwable instanceof JsonParseException || throwable instanceof JSONException || throwable instanceof java.text.ParseException) {
            ce = new Exception("解析数据出错", throwable);
            commonException(ce);
        } else if (throwable instanceof SocketTimeoutException) {
            ce = new Exception("连接超时", throwable);
            commonException(ce);
        } else if (throwable instanceof ConnectException) {
            ce = new Exception("服务器异常", throwable);
            commonException(ce);
        } else if (!parseException(throwable)) {
            ce = new Exception("未知错误", throwable);
            commonException(ce);
        }
    }
}
