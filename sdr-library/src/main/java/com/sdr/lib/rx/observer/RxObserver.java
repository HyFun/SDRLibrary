package com.sdr.lib.rx.observer;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.orhanobut.logger.Logger;
import com.sdr.lib.http.HttpClient;
import com.sdr.lib.mvp.AbstractView;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.ResourceObserver;
import retrofit2.HttpException;

/**
 * Created by HyFun on 2019/06/06.
 * Email: 775183940@qq.com
 * Description:
 */
public abstract class RxObserver<T, V extends AbstractView> extends ResourceObserver<T> {

    private List<ExceptionTransformer> transformerList = new ArrayList<>();

    /**
     * 当不需要提示时 使用这个构造方法
     */
    public RxObserver() {
    }

    private V mView;

    /**
     * 当需要提示在页面上显示时，使用该构造方法
     */
    public RxObserver(V mView) {
        this.mView = mView;
        onExceptionSolve(transformerList);
    }


    @Override
    public void onError(Throwable e) {
        // 打印出异常信息
        Logger.t(HttpClient.TAG).e(e, TextUtils.isEmpty(e.getMessage()) ? "No errMsg!" : e.getMessage());
        if (mView != null) {
            // 处理异常
            boolean solved = false;
            for (ExceptionTransformer transformer : transformerList) {
                if (transformer.getExceptionClass().isInstance(e)) {
                    ExceptionSolver solver = transformer.getSolver();
                    if (solver != null) {
                        solver.solve(mView, e);
                    }
                    solved = true;
                    break;
                }
            }

            if (!solved) {
                mView.showErrorMsg("未知错误", e.getMessage());
            }

        }
        onComplete();
    }

    /**
     * 处理异常
     */
    public void onExceptionSolve(List<ExceptionTransformer> transformerList) {
        transformerList.add(new ExceptionTransformer(ConnectException.class, new ExceptionSolver<V>() {
            @Override
            public void solve(V v, Throwable throwable) {
                v.showErrorMsg("连接异常", throwable.getMessage());
            }
        }));

        transformerList.add(new ExceptionTransformer(HttpException.class, new ExceptionSolver<V>() {
            @Override
            public void solve(V v, Throwable throwable) {
                HttpException httpException = (HttpException) throwable;
                v.showErrorMsg("网络连接异常：" + httpException.code(), httpException.message());
            }
        }));

        transformerList.add(new ExceptionTransformer(SocketTimeoutException.class, new ExceptionSolver() {
            @Override
            public void solve(AbstractView abstractView, Throwable throwable) {
                abstractView.showErrorMsg("连接超时", "服务器长时间没有响应");
            }
        }));


        transformerList.add(new ExceptionTransformer(JsonParseException.class, new ExceptionSolver() {
            @Override
            public void solve(AbstractView abstractView, Throwable throwable) {
                abstractView.showErrorMsg("数据解析错误", "json数据解析错误");
            }
        }));

        transformerList.add(new ExceptionTransformer(JSONException.class, new ExceptionSolver() {
            @Override
            public void solve(AbstractView abstractView, Throwable throwable) {
                abstractView.showErrorMsg("数据解析错误", "json数据解析错误");
            }
        }));

        transformerList.add(new ExceptionTransformer(java.text.ParseException.class, new ExceptionSolver() {
            @Override
            public void solve(AbstractView abstractView, Throwable throwable) {
                abstractView.showErrorMsg("数据解析错误", "json数据解析错误");
            }
        }));
    }

}
