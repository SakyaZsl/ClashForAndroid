package com.github.kr328.clash.rocket.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.kr328.clash.MainActivity
import com.github.kr328.clash.R
import com.github.kr328.clash.common.network.bean.UserInfoData
import com.github.kr328.clash.common.network.http.RetrofitHelper
import com.github.kr328.clash.common.network.http.call.Callback
import com.github.kr328.clash.common.network.service.LoginApiService
import com.github.kr328.clash.common.util.Constants.Login_Type.TYPE_EMAIL
import com.github.kr328.clash.common.util.Constants.Login_Type.TYPE_PHONE
import com.github.kr328.clash.design.databinding.ActivityLoginPasswordBinding
import com.github.kr328.clash.design.util.RegexUtils
import com.github.kr328.clash.design.util.ToastUtils
import com.github.kr328.clash.design.util.root
import retrofit2.Call
import retrofit2.Response

class LoginPasswordActivity : AppCompatActivity() {
    companion object {
        fun startAction(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, LoginPasswordActivity::class.java))
        }
    }

    private lateinit var binding: ActivityLoginPasswordBinding
    private var isPhone = true
    private lateinit var loginApi: LoginApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPasswordBinding.inflate(layoutInflater, root, false)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        loginApi = RetrofitHelper.create(LoginApiService::class.java)
        binding.tvLoginChange.setOnClickListener {
            isPhone = !isPhone
            changeLayoutByType()
        }
        binding.btnLogin.setOnClickListener {
            if (checkInfo()) {
                loginByPassword()
            }
        }
        binding.ivClearContent.setOnClickListener {
            binding.etLoginContent.setText("")
        }
        binding.ivClearPassword.setOnClickListener {
            binding.etUserPassword.setText("")
        }

    }

    private fun loginByPassword() {
        var type: String = if (isPhone) {
            TYPE_PHONE
        } else {
            TYPE_EMAIL
        }
        loginApi.loginByPassword(
            type,
            binding.etLoginContent.text.toString(),
            binding.etUserPassword.text.toString()
        ).enqueue(object : Callback<UserInfoData> {
            override fun onResponse(call: Call<UserInfoData>, response: Response<UserInfoData>) {
                Log.e("zzzz", "onResponse:${response.body()} ")
                saveUserInfo()
                startActivity(Intent(this@LoginPasswordActivity,MainActivity::class.java))
            }

            override fun onFailure(call: Call<UserInfoData>, t: Throwable) {
                ToastUtils.showMessage(this@LoginPasswordActivity,t.message)
                Log.e("zzzz", "onFailure:$call t:$t")
            }

            override fun onStart(call: Call<UserInfoData>?) {
                Log.e("zzzz", "onStart:$call ")
            }

            override fun onCompleted(call: Call<UserInfoData>?) {
                Log.e("zzzz", "onCompleted:$call ")
            }

        })
    }

    private fun saveUserInfo(){

    }

    private fun checkInfo(): Boolean {
        val content = binding.etLoginContent.text.toString()
        if (isPhone) {
            if (RegexUtils.checkPhone(content)) {
                if (binding.etUserPassword.text.toString().length >= 8) {
                    return true
                }
                ToastUtils.showMessage(this, getString(R.string.tip_password_less))
                return false
            }
            ToastUtils.showMessage(this, getString(R.string.tip_phone_error))
            return false
        }
        if (RegexUtils.checkEmail(content)) {
            if (binding.etUserPassword.text.toString().length >= 8) {
                return true
            }
            ToastUtils.showMessage(this, getString(R.string.tip_password_less))
            return false
        }
        ToastUtils.showMessage(this, getString(R.string.tip_email_error))
        return false
    }

    private fun changeLayoutByType() {
        if (isPhone) {
            binding.tvLoginPre.text = getText(R.string.code_cn)
            binding.tvLoginChange.text = getText(R.string.login_by_phone)
            return
        }
        binding.tvLoginPre.text = getText(R.string.email)
        binding.tvLoginChange.text = getText(R.string.login_by_email)
    }
}