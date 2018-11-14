package com.video.longwu.util;

import com.video.longwu.BaseApplication;
import com.video.longwu.http.interceptor.BaseInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SimpleOkhttpClientUtil<T> {
    private static SimpleOkhttpClientUtil clientUtil;

    public static SimpleOkhttpClientUtil getInstant() {
        if (clientUtil == null) {
            synchronized (SimpleOkhttpClientUtil.class) {
                clientUtil = new SimpleOkhttpClientUtil();
            }
        }
        return clientUtil;
    }

    public T getApi(String baseUrl, Class<T> tClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(tClass);
    }

    static OkHttpClient mOkHttpClient;

    public static void initOkHttpClient() {
        BaseInterceptor baseInterceptor = new BaseInterceptor.Builder()
                .build();
        if (mOkHttpClient == null) {
            synchronized (SimpleOkhttpClientUtil.class) {
                if (mOkHttpClient == null) {
                    // 指定缓存路径,缓存大小100Mb
                    Cache cache = new Cache(new File(BaseApplication.getContext().getCacheDir(),
                            "HttpCache"), 1024 * 1024 * 100);
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(baseInterceptor)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(interceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    // 云端响应头拦截器，用来配置缓存策略
    private static Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            try {
                //有网络
                request = request.newBuilder()
                        .build();
                Response originalResponse = chain.proceed(request);
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .removeHeader("Pragma")
                        .build();

            } catch (IOException e) {
                return chain.proceed(request);
            }

        }
    };
}
