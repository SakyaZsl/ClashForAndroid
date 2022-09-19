package com.github.kr328.clash.common.network.bean

data class EmailRegisterRequest(var email: String, var nickName: String, var passWord: String)
data class PhoneRegisterRequest(var phone: String, var nickName: String, var passWord: String)
