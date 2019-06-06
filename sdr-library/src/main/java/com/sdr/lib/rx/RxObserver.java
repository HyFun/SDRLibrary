package com.sdr.lib.rx;

import com.orhanobut.logger.Logger;
import com.sdr.lib.http.HttpClient;
import com.sdr.lib.mvp.AbstractView;

import io.reactivex.observers.ResourceObserver;

/**
 * Created by HyFun on 2019/06/06.
 * Email: 775183940@qq.com
 * Description:
 */
public abstract class RxObserver<T> extends ResourceObserver<T> {

    private AbstractView mView;

    public RxObserver(AbstractView mView) {
        this.mView = mView;
    }

    @Override
    public void onError(Throwable e) {
        if (mView == null) {
            return;
        }
        // 处理异常
        RxUtils.handleException(new HandleException(e) {
            @Override
            public boolean parseException(Throwable throwable) {
                if (throwable instanceof ServerException) {
                    mView.showErrorMsg(throwable.getMessage());
                    return true;
                } else if (throwable instanceof IgnoreException) {
                    // 忽略此Exception
                    return true;
                }
                return false;
            }

            @Override
            public void commonException(Exception exception) {
                mView.showErrorMsg(exception.getMessage());
            }
        });
        // 打印出异常信息
        Logger.t(HttpClient.TAG).e(e, e.getMessage());
        onComplete();
    }
}
