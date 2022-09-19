package com.github.kr328.clash.common.network.http;

import retrofit2.Retrofit;

/**
 * Create by Carson on 2021/10/18.
 * 功能描述：管理全局的Retrofit实例,外观模式
 */
public class RetrofitHelper {
    /**
     * 全局的Retrofit对象,like Charset#bugLevel,HttpLoggingInterceptor#level,
     * AsyncTask#mStatus,facebook->stetho->LogRedirector#sLogger,Timber->forestAsArray
     * CopyOnWriteArrayList==
     */
    public static volatile Retrofit DEFAULT;

    private RetrofitHelper() {
        throw new AssertionError("No instances.");
    }

    public static <T> T create(Class<T> service) {
        //确保多线程的情况下retrofit不为空或者被修改了
        Retrofit retrofit = DEFAULT;
        if (retrofit == null) {
            throw new IllegalStateException("DEFAULT == null");
        }
        return retrofit.create(service);
    }
}
