package com.sdr.lib.rx;

import com.google.gson.JsonParseException;
import com.sdr.lib.SDRLibrary;
import com.sdr.lib.util.CommonUtil;

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

    public abstract void commonException(CommonException exception);

    private void handleException() {
        CommonException ce = new CommonException(throwable);
        if (!CommonUtil.isNetworkConnected(SDRLibrary.getInstance().getApplication())) {
            // 说明没网
            ce.message = "网络不给力，请检查网络设置";
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
                    ce.message = "网络连接错误";  //均视为网络错误
                    break;
            }
        } else if (throwable instanceof JsonParseException || throwable instanceof JSONException || throwable instanceof java.text.ParseException) {
            ce.message = "解析数据出错";            //均视为解析错误
        } else if (throwable instanceof SocketTimeoutException) {
            ce.message = "连接超时";
        } else if (throwable instanceof ConnectException) {
            ce.message = "服务器异常";  //均视为网络错误
        } else if (!parseException(throwable)) {
            ce.message = "未知错误";          //未知错误
        }
        commonException(ce);
    }
}
