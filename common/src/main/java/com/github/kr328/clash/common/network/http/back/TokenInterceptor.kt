package com.github.kr328.clash.common.network.http.back

import android.util.Log
import com.github.kr328.clash.common.util.Constants.Urls.token
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class TokenInterceptor :Interceptor {
    private val TAG = "TokenInterceptor"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)
        Log.d(TAG, "response.code=" + response.code())

        //根据和服务端的约定判断token过期
        if (isTokenExpired(response)) {
            Log.d(TAG, "自动刷新Token,然后重新请求数据")
            //同步请求方式，获取最新的Token
            val newToken = getNewToken()
            //使用新的Token，创建新的请求
            val newRequest: Request = chain.request()
                .newBuilder()
                .header("x-token", newToken)
                .build()
            //重新请求
            return chain.proceed(newRequest)
        }
        return response
    }

    /**
     * 根据Response，判断Token是否失效
     *
     * @param response
     * @return
     */
    private fun isTokenExpired(response: Response): Boolean {
//        return response.code() == 301
        return true
    }

    /**
     * 同步请求方式，获取最新的Token
     *
     * @return
     */
    @Throws(IOException::class)
    private fun getNewToken(): String {
        // 通过获取token的接口，同步请求接口
        return token
    }
}