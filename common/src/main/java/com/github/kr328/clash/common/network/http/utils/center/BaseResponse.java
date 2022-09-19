package com.github.kr328.clash.common.network.http.utils.center;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Create by Carson on 2021/10/18.
 * 返回基类封装
 */
public class BaseResponse<T> implements Serializable {

    private int errorCode;   //status为0||200正常
    private T data; // 返回数据，不同接口数据格式不同。
    private String errorMsg;


    public BaseResponse(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
