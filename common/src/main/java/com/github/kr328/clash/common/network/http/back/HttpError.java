package com.github.kr328.clash.common.network.http.back;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import retrofit2.HttpException;

/**
 * Create by Carson on 2021/12/23.
 * 通用的错误信息，一般请求是失败只需要弹出一些错误信息即可,like{@link HttpException}
 */
public final class HttpError extends RuntimeException {
    private static final long serialVersionUID = -134024482758434333L;
    /**
     * 展示在前端的错误描述信息
     */
    @NonNull
    public String errorMessage;
    @NonNull
    public int errorCode;

    /**
     * <p>
     * 请求失败保存失败信息,for example:
     * <li>1.original json:  原始的json</li>
     * <li>2.Throwable: 抛出的异常信息{@link HttpException}</li>
     * <li>3.自定义的一些对象</li>
     * </p>
     */
    @Nullable
    public final transient Object body;

    public HttpError(String errorMessage, int errorCode) {
        this(errorMessage, errorCode, null);
    }

    public HttpError(String errorMessage, int errorCode, @Nullable Object body) {
        super(errorMessage);
        if (body instanceof Throwable) {
            initCause((Throwable) body);
        }
        //FastPrintWriter#print(String str)
        this.errorMessage = errorMessage != null ? errorMessage : "null";
        this.errorCode = errorCode;
        this.body = body;
    }

    /**
     * {@link okhttp3.Request#tag(Class)}
     * {@link android.os.Bundle#getInt(String)}
     * 转换body为想要的类型，如果类型不匹配，则返回null
     *
     * @param <T> 泛型用于指定类型
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T castBody() {
        try {
            return (T) body;
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * 保证和msg一致
     */
    @Override
    public String getMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "HttpError {errorMessage="
                + errorMessage
                + ",errorCode="
                + errorCode
                + ", body="
                + body
                + '}';
    }
}