package com.sdr.lib.mvp;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by HyFun on 2018/11/22.
 * Email: 775183940@qq.com
 * Description:
 */

public class BasePresenter<V extends AbstractView> implements AbstractPresenter<V> {
    public int pageNo = 1;
    public final int pageSize = 20;
    protected V view;

    private CompositeDisposable compositeDisposable;


    @Override
    public void attachView(V v) {
        view = v;
    }

    @Override
    public void detachView() {
        view = null;
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    @Override
    public void addSubscription(Disposable... disposables) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.addAll(disposables);
    }
}
