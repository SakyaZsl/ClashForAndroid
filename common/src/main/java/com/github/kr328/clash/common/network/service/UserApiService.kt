package com.github.kr328.clash.common.network.service

import com.github.kr328.clash.common.network.http.call.CompletableCall
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface UserApiService {

    @GET("/app/customer/info")
    fun getUserInfo(@Header("x-token") token:String): CompletableCall<String>
}