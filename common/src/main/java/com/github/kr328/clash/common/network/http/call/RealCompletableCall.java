package com.github.kr328.clash.common.network.http.call;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Create by Carson on 2021/12/23.
 */
final class RealCompletableCall<T> implements CompletableCall<T> {
    private final Executor callbackExecutor;
    private final Call<T> delegate;

    RealCompletableCall(Executor callbackExecutor, Call<T> delegate) {
        this.callbackExecutor = callbackExecutor;
        this.delegate = delegate;
    }

    @SuppressLint("NewApi")
    @Override
    public void enqueue(final Callback<T> callback) {
        Objects.requireNonNull(callback, "callback==null");
        callbackExecutor.execute(() -> callback.onStart(delegate));
        delegate.enqueue(new retrofit2.Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                //大道至简
                callbackExecutor.execute(() -> {
                    if (delegate.isCanceled()) {
                        // Emulate OkHttp's behavior of throwing/delivering an IOException on
                        // cancellation.
                        callback.onFailure(delegate, new IOException("Canceled"));
                    } else {
                        callback.onResponse(delegate, response);
                    }
                    callback.onCompleted(delegate);
                });
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                callbackExecutor.execute(() -> {
                    callback.onFailure(delegate, t);
                    callback.onCompleted(delegate);
                });
            }
        });
    }

    @Override
    public Call<T> delegate() {
        return delegate;
    }

    @NonNull
    @Override
    public CompletableCall<T> clone() {
        return new RealCompletableCall<>(callbackExecutor, delegate.clone());
    }
}
