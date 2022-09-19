package com.github.kr328.clash.common.network.service

import com.github.kr328.clash.common.network.bean.EmailRegisterRequest
import com.github.kr328.clash.common.network.bean.PhoneRegisterRequest
import com.github.kr328.clash.common.network.bean.UserInfoData
import com.github.kr328.clash.common.network.http.call.CompletableCall
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApiService {
    @GET("/base/argument")
    fun getArgument(@Query("title") title: String): CompletableCall<String>

    @POST("/common/verify/code/email")
    fun sendEmailCode(
        @Query("scenes") title: String,
        @Query("value") email: String
    ): CompletableCall<Any?>

    @POST("/common/verify/code/sms")
    fun sendPhoneCode(
        @Query("scenes") title: String,
        @Query("value") email: String
    ): CompletableCall<String>

    @GET("/common/verify/code/sms/check")
    fun verifyPhoneCode(
        @Query("scenes") title: String,
        @Query("value") phone: String,
        @Query("code") code: String,
    ): CompletableCall<String>

    @GET("/common/verify/code/email/check")
    fun verifyEmailCode(
        @Query("scenes") title: String,
        @Query("value") phone: String,
        @Query("code") code: String,
    ): CompletableCall<String>


    @POST("/base/register/phone")
    fun registerByPhone(@Body body: PhoneRegisterRequest): CompletableCall<UserInfoData>

    @POST("/base/register/email")
    fun registerByEmail(@Body body: EmailRegisterRequest): CompletableCall<UserInfoData>

    @GET("/base/pwd/login")
    fun loginByPassword(@Query("types") type: String,
                             @Query("value") phone: String,
                             @Query("pwd") pwd: String,): CompletableCall<UserInfoData>

    @GET("/base/code/login")
    fun loginByCode(@Query("types") title: String,
                             @Query("value") phone: String,
                             @Query("code") code: String,): CompletableCall<String>
}