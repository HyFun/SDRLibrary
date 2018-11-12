package com.sdr.lib.mvp;

import io.reactivex.disposables.Disposable;

/**
 * Created by HyFun on 2018/10/12.
 * Email:775183940@qq.com
 */

public interface AbstractPresenter<T extends AbstractView> {
    void attachView(T view);

    void detachView();

    void addSubscription(Disposable... disposables);
}
