package com.sdr.lib.http;

import com.google.gson.Gson;
import com.sdr.lib.SDRLibrary;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HYF on 2018/10/27.
 * Email：775183940@qq.com
 */

public class HttpClient {
    public static final String TAG = "OkHttp";


    public static final Gson gson = new Gson();

    private static HttpClient httpClient;

    private OkHttpClient okHttpClient;

    private HttpClient() {
        // 初始化okhttp client
        okHttpClient = createOkHttpClient();
    }

    public static HttpClient getInstance() {
        if (httpClient == null) {
            synchronized (HttpClient.class) {
                if (httpClient == null) {
                    httpClient = new HttpClient();
                }
            }
        }
        return httpClient;
    }

    public <T> T createRetrofit(String url, OkHttpClient okHttpClient, Class<T> clazz) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient);
        builder.baseUrl(url);//设置远程地址
        builder.addConverterFactory(new NullOnEmptyConverterFactory());
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return builder.build().create(clazz);
    }


    /**
     * 获取okhttp client  实例
     *
     * @return
     */
    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * 创建一个okhttp client实例
     *
     * @return
     */

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 缓存  只会存放get缓存 存放在data/data/cache/NetCache文件夹下
        File cacheFile = new File(HttpClientUtil.getNetCachePath(SDRLibrary.getInstance().getApplication().getApplicationContext()));
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        // 超时时间 30秒
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        //设置缓存
        AddCacheInterceptor cacheInterceptor = new AddCacheInterceptor(SDRLibrary.getInstance().getApplication().getApplicationContext());
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        // interceptor  log
        builder.addNetworkInterceptor(getLoggingInterceptor(SDRLibrary.getInstance().isDebug()));
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }


    /**
     * 日志的Interceptor
     *
     * @return
     */
    private HttpLoggingInterceptor getLoggingInterceptor(boolean debug) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLogger());
        if (debug) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 测试
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE); // 打包
        }
        return interceptor;
    }

}
