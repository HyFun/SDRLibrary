package com.sdr.lib.rx.observer;

import com.sdr.lib.mvp.AbstractView;

/**
 * Created by HyFun on 2019/11/8.
 * Email: 775183940@qq.com
 * Description:
 */
public interface ExceptionSolver<T extends AbstractView> {
    void solve(T t, Throwable throwable);
}
