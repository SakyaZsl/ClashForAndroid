package com.github.kr328.clash.common.network.http.call;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.github.kr328.clash.common.network.http.back.LifecycleCallback;

import java.util.Objects;

import retrofit2.Call;

/**
 * Create by Carson on 2021/12/23.
 * 功能描述：支持生命周期绑定的Call{@link Call}
 *
 * @param <T> Successful response body type.
 */
public interface CompletableCall<T> extends Cloneable {

    Call<T> delegate();

    /**
     * Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred talking to the server, creating the request, or processing the response.
     */
    void enqueue(Callback<T> callback);

    /**
     *
     * @param callback 回调函数
     * @param owner    LifecycleOwner ,当owner当前的状态为{@link }
     *                 不会调用任何回调函数
     */
    @SuppressLint("NewApi")
    default void enqueue(@Nullable LifecycleOwner owner, Callback<T> callback) {
        Objects.requireNonNull(callback, "callback==null");
        enqueue(owner != null ? new LifecycleCallback<>(this, callback, owner) : callback);
    }

    /**
     * Create a new, identical httpQueue to this one which can be enqueued even if this httpQueue
     * has already been.
     */
    @NonNull
    CompletableCall<T> clone();
}