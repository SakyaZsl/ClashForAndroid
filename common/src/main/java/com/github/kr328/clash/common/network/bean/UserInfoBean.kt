package com.github.kr328.clash.common.network.bean

data class UserInfoBean(
    var ID: Int? = 0,
    var uuid: String? = "",
    var userName: String? = "",
    var nickName: String? = "",
    var email: String? = "",
    var phone: String? = "",
    var birthday: String? = "",
    var sex: String? = "",
    var avatarUrl: String? = "",
    var status: String? = ""
)

data class UserInfoData(var user: UserInfoBean? = null,var token:String?="")