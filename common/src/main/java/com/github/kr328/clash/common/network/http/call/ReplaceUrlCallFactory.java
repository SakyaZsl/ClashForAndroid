package com.github.kr328.clash.common.network.http.call;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Create by Carson on 2021/12/23.
 * 功能描述： 替换修改{@link Request#url()}
 */
public abstract class ReplaceUrlCallFactory extends CallFactoryProxy {

    public ReplaceUrlCallFactory(@NonNull Call.Factory delegate) {
        super(delegate);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public final Call newCall(@NonNull Request request) {
        /*
         * 动态替换baseUrl
         */
        String baseUrlName = request.header("BaseUrlName");
        if (baseUrlName != null) {
            HttpUrl newHttpUrl = getNewUrl(baseUrlName, request);
            if (newHttpUrl != null) {
                Request newRequest = request.newBuilder()
                        .url(newHttpUrl)
                        .build();
                return delegate.newCall(newRequest);
            }
        }
        /*
         * 添加头部请求参数
         */
        Request newRequest = request.newBuilder()
                .build();
        return delegate.newCall(newRequest);
    }

    /**
     * @param baseUrlName name to sign baseUrl
     * @return new httpUrl, if null use old httpUrl
     */
    @Nullable
    protected abstract HttpUrl getNewUrl(String baseUrlName, Request request);
}
