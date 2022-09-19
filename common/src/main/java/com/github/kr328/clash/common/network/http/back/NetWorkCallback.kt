package com.model.common.utils.http.back

import android.app.Activity
import android.content.Context
import com.github.kr328.clash.common.network.http.back.BodyCallback
import com.github.kr328.clash.common.network.http.back.HttpError
import com.google.gson.JsonSyntaxException
import retrofit2.Call

/**
 * Create by Carson on 2021/12/23.
 * 网络请求返回处理
 */
abstract class NetWorkCallback<T>(val mContext: Context) : BodyCallback<T>() {

    override fun onStart(call: Call<T>?) {

    }

    override fun parseThrowable(call: Call<T>?, t: Throwable?): HttpError {
        return if (t is JsonSyntaxException) {
            HttpError("解析异常", -100, t)
        } else super.parseThrowable(call, t)
    }

    override fun onCompleted(call: Call<T>?) {

    }
}