package com.sdr.lib.rx.observer;

import com.orhanobut.logger.Logger;
import com.sdr.lib.http.HttpClient;
import com.sdr.lib.mvp.AbstractView;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.ResourceObserver;

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

    protected V mView;

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
        Logger.t(HttpClient.TAG).e(e, e.getMessage());
        if (mView != null) {
            // 处理异常
            for (ExceptionTransformer transformer : transformerList) {
                if (e.getClass().isInstance(transformer.getExceptionClass())) {
                    ExceptionSolver solver = transformer.getSolver();
                    if (solver != null) {
                        solver.solve(e);
                    }
                } else {
                    // 没有处理的类
                    mView.showErrorMsg("未知错误", e.getMessage());
                }
            }
        }
        onComplete();
    }

    /**
     * 处理异常
     */
    public void onExceptionSolve(List<ExceptionTransformer> transformerList) {
        transformerList.add(new ExceptionTransformer(ConnectException.class, new ExceptionSolver() {
            @Override
            public void solve(Throwable throwable) {

            }
        }));

    }

}
