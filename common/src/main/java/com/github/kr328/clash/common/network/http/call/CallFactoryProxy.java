package com.github.kr328.clash.common.network.http.call;

import android.annotation.SuppressLint;

import java.util.Objects;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Create by Carson on 2021/12/23.
 * 功能描述：代理{@link Call.Factory} 拦截{@link #newCall(Request)}方法
 */
public abstract class CallFactoryProxy implements Call.Factory {

    protected final Call.Factory delegate;

    @SuppressLint("NewApi")
    public CallFactoryProxy(Call.Factory delegate) {
        Objects.requireNonNull(delegate, "delegate==null");
        this.delegate = delegate;
    }
}
