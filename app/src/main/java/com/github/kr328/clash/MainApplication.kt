package com.github.kr328.clash

import android.app.Application
import android.content.Context
import com.github.kr328.clash.common.Global
import com.github.kr328.clash.common.compat.currentProcessName
import com.github.kr328.clash.common.log.Log
import com.github.kr328.clash.common.network.http.RetrofitHelper
import com.github.kr328.clash.common.network.http.back.LogInterceptor
import com.github.kr328.clash.common.network.http.call.CompletableCallAdapterFactory
import com.github.kr328.clash.common.network.http.call.ReplaceUrlCallFactory
import com.github.kr328.clash.common.network.http.converter.GsonConverterFactory
import com.github.kr328.clash.common.util.Constants
import com.github.kr328.clash.remote.Remote
import com.github.kr328.clash.service.util.sendServiceRecreated
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Suppress("unused")
class MainApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        Global.init(this)
    }

    override fun onCreate() {
        super.onCreate()

        val processName = currentProcessName
        initHttp()
        Log.d("Process $processName started")

        if (processName == packageName) {
            Remote.launch()
        } else {
            sendServiceRecreated()
        }
    }

    fun finalize() {
        Global.destroy()
    }

    /**
     * 初始化网络请求
     */
    private fun initHttp() {
        val logInterceptor = LogInterceptor { message ->
            android.util.Log.e(
                "httpMessage",
                message
            )
        }
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(logInterceptor)
            .connectTimeout(6L, TimeUnit.SECONDS)
            .readTimeout(6L, TimeUnit.SECONDS)
            .build()
        RetrofitHelper.DEFAULT = Retrofit.Builder()
            .baseUrl(Constants.Urls.BASE_URL)
            .callFactory(object : ReplaceUrlCallFactory(client) {
                override fun getNewUrl(baseUrlName: String, request: Request): HttpUrl? {
                    val oldUrl = request.url().toString()
                    if (baseUrlName == "city") {
                        val newUrl: String = oldUrl.replace(
                            Constants.Urls.BASE_URL,
                            "https://staticyx.lnetco.com/"
                        )
                        return HttpUrl.get(newUrl)
                    }
                    return null
                }
            })
            .addCallAdapterFactory(CompletableCallAdapterFactory.INSTANCE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}