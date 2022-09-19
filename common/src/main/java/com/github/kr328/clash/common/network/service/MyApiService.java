package com.github.kr328.clash.common.network.service;


import com.github.kr328.clash.common.network.http.call.CompletableCall;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyApiService {
    public static final String  cityUrl = "BaseUrlName:city";
    /**
     * 获取省市区信息
     */
    @GET("/base/argument")
    CompletableCall<String> getArgument(@Query("title") String title);

    @GET("/common/verify/code/email")
    CompletableCall<String> sendSmsCode(@Query("title") String title);


}
