package com.github.kr328.clash.common.util

class Constants {
    object Urls {
        const val BASE_URL = "http://118.195.131.240:10008/"
        const val MZSM_KEY = "MZSM_zh_HK"
        const val FWXY_KEY = "FWXY_zh_HK"
        const val token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVVUlEIjoiOTliMzk4YTMtZjc4Ny00MDExLWJhZmQtNDViMTk5YTk4ZDhhIiwiVXNlcm5hbWUiOiIiLCJJRCI6MTk2LCJFbWFpbCI6ImFzZGRkQHFxLmNvbSIsIlRlbGVwaG9uZSI6IiIsIk5pY2tOYW1lIjoiaGhoaCIsIlN0YXR1cyI6MSwiQXV0aG9yaXR5SWQiOjAsIkJ1ZmZlclRpbWUiOjg2NDAwLCJleHAiOjE2NjQwOTU5NzEsImlzcyI6InFtUGx1cyIsIm5iZiI6MTY2MzQ5MDE3MX0.F4p6ju1W2KfUxq2HPaM_UOl4itGfNmU3MaoTibWjyAk"

    }
    object Email {
        //注册使用
        const val NEW_EMAIL = "newEmail"
        //登录使用
        const val LOGIN = "login"
        //修改密码
        const val RESET_PASSWORD = "resetPwd"
        //绑定邮箱
        const val BIND_EMAIL = "bindEmail"
        //修改邮箱
        const val RESET_EMAIL = "resetEmail"
    }
    object Phone {
        //注册使用
        const val NEW_PHONE = "newPhone"
        //绑定手机
        const val BIND_PHONE = "bindPhone"
        //修改手机
        const val RESET_PHONE = "resetPhone"
    }
    object Login_Type{
        //注册类型
        const val TYPE_EMAIL = "email"
        const val TYPE_PHONE = "telephone"
    }
    object Mock {
        //注册使用
        const val TEST_EMAIL = "389093945@qq.com"
        const val TEST_PHONE = "18101365423"
        const val TEST_CODE = "123456"
    }

    object Intent_Key{
        const val KEY_USER_PHONE = "user_phone"
        const val KEY_IS_PHONE = "is_phone"
        const val KEY_SMS_CODE = "sms_code"
    }

}