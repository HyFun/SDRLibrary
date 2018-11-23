package com.sdr.lib.rx;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chao.qu at 2017/10/20
 *
 * @author quchao
 */

public class RxUtils {
    /**
     * 统一线程处理
     *
     * @param <T> 指定的泛型类型
     * @return ObservableTransformer
     */
    public final static <T> ObservableTransformer<T, T> io_main() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 处理异常
     *
     * @param handle
     */
    public final static void handleException(HandleException handle) {
    }
}