package com.sdr.sdrlib.http;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by HyFun on 2019/06/06.
 * Email: 775183940@qq.com
 * Description:
 */
public interface API {
    @GET
    Observable<ResponseBody> getBaidu(@Url String url);
}
