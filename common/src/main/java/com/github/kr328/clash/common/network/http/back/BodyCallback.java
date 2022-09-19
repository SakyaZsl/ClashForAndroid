package com.github.kr328.clash.common.network.http.back;

import androidx.annotation.NonNull;

import com.github.kr328.clash.common.network.http.call.Callback;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Create by Carson on 2021/12/23.
 * 网络请求返回基类
 */
public abstract class BodyCallback<T> implements Callback<T> {
    @Override
    public final void onResponse(@NonNull Call<T> call, Response<T> response) {
        T body = response.body();
        if (body != null) {
            onSuccess(call, body);
        } else {
            onFailure(call, new HttpException(response));
        }
    }

    @Override
    public final void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        HttpError error = parseThrowable(call, t);
        Objects.requireNonNull(error);
        onError(call, error);
    }

    protected abstract void onError(Call<T> call, HttpError error);

    protected abstract void onSuccess(Call<T> call, T response);

    /**
     * @param t 统一解析throwable对象转换为HttpError对象，
     *          <ul>
     *          <li>如果throwable为{@link HttpError}则为{@link retrofit2.Converter#convert(Object)}内抛出的异常</li>
     *          <li>如果为{@link HttpException}则为{@link Response#body()}为null的时候抛出的</li>
     *          <ui/>
     * @see #onFailure(Call, Throwable)
     */
    @NonNull
    protected HttpError parseThrowable(Call<T> call, Throwable t) {
        if (t instanceof HttpError) {
            return (HttpError) t;
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            final String msg;
            switch (httpException.code()) {
                case 400:
                    msg = "参数错误";
                    break;
                case 401:
                    msg = "身份未授权";
                    break;
                case 403:
                    msg = "禁止访问";
                    break;
                case 404:
                    msg = "地址未找到";
                    break;
                default:
                    msg = "服务异常";
            }
            return new HttpError(msg, httpException.code(), httpException);
        } else if (t instanceof UnknownHostException) {
            return new HttpError("网络异常", -400, t);
        } else if (t instanceof ConnectException) {
            return new HttpError("连接异常", -101, t);
        } else if (t instanceof SocketException) {
            return new HttpError("服务异常", -102, t);
        } else if (t instanceof SocketTimeoutException) {
            return new HttpError("网络连接超时", -103, t);
        } else {
            return new HttpError("请求失败", -1, t);
        }
    }

}
