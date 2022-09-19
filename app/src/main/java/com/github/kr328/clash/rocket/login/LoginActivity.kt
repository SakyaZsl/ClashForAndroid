package com.github.kr328.clash.rocket.login

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.kr328.clash.R
import com.github.kr328.clash.common.network.http.RetrofitHelper
import com.github.kr328.clash.common.network.http.call.Callback
import com.github.kr328.clash.common.network.service.LoginApiService
import com.github.kr328.clash.common.util.Constants.Email.LOGIN
import com.github.kr328.clash.common.util.Constants.Email.NEW_EMAIL
import com.github.kr328.clash.common.util.Constants.Mock.TEST_CODE
import com.github.kr328.clash.common.util.Constants.Mock.TEST_EMAIL
import com.github.kr328.clash.common.util.Constants.Mock.TEST_PHONE
import com.github.kr328.clash.common.util.Constants.Phone.NEW_PHONE
import com.github.kr328.clash.design.databinding.ActivityLoginBinding
import com.github.kr328.clash.design.util.RegexUtils
import com.github.kr328.clash.design.util.ToastUtils
import com.github.kr328.clash.design.util.root
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    companion object {
        const val MSG_TIME = 200;
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginApi: LoginApiService
    private var isPhone = true
    private var leftTime = 60
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater, root, false)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        loginApi = RetrofitHelper.create(LoginApiService::class.java)
        binding.tvToRegister.setOnClickListener {
            RegisterActivity.startAction(this)
        }
        binding.tvLoginChange.setOnClickListener {
            changeLayoutByType()
        }
        binding.tvSendCode.setOnClickListener {
            leftTime = 60

            if (isPhone) {
                if (checkPhone()) {
                    handler.post(runnable)
                    sendPhoneCode()
                }
                return@setOnClickListener
            }
            if (checkEmail()) {
                handler.post(runnable)
                sendEmailCode()
            }
        }
        binding.llLoginPassword.setOnClickListener {
            LoginPasswordActivity.startAction(this)
        }
        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun changeLayoutByType() {
        if (isPhone) {
            isPhone = false
            binding.tvLoginPre.text = getString(R.string.email)
            binding.tvLoginChange.text = getString(R.string.login_by_email)
            binding.etLoginContent.setHint(R.string.email_hint)
            return
        }
        isPhone = true
        binding.tvLoginPre.text = getString(R.string.code_cn)
        binding.tvLoginChange.text = getString(R.string.login_by_phone)
        binding.etLoginContent.setHint(R.string.phone_hint)
    }

    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_TIME -> {
                    if (leftTime > 0) {
                        binding.tvSendCode.isEnabled = false
                        binding.tvSendCode.text = getString(R.string.login_count_down, leftTime)
                    } else {
                        binding.tvSendCode.isEnabled = true
                        binding.tvSendCode.text = getString(R.string.code_send)
                    }
                    leftTime--
                    postDelayed(runnable, 1000)
                }
                else -> {}
            }
        }
    }

    private val runnable = Runnable {
        val msg = Message.obtain()
        msg.what = MSG_TIME
        msg.obj = leftTime
        handler.sendMessage(msg)
    }


    private fun sendEmailCode() {
        loginApi.sendEmailCode(NEW_EMAIL, TEST_EMAIL).enqueue(object : Callback<Any?> {
            override fun onResponse(call: Call<Any?>, response: Response<Any?>) {
                Log.e("zzzz", "onResponse: ${response.body()}")

            }

            override fun onFailure(call: Call<Any?>, t: Throwable) {
                Log.e("zzzz", "onFailure: $call ====t:$t")
            }

            override fun onStart(call: Call<Any?>?) {
                Log.e("zzzz", "onStart: $call")
            }

            override fun onCompleted(call: Call<Any?>?) {
                Log.e("zzzz", "onCompleted: $call")
            }

        })
    }

    private fun sendPhoneCode() {
        loginApi.sendPhoneCode(LOGIN, TEST_PHONE).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.e("zzzz", "onResponse: ${response.body()}")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("zzzz", "onFailure: $call ====t:$t")
            }

            override fun onStart(call: Call<String>?) {
                Log.e("zzzz", "onStart: $call")
            }

            override fun onCompleted(call: Call<String>?) {
                Log.e("zzzz", "onCompleted: $call")
            }

        })
    }

    private fun verifyPhoneCode() {
        loginApi.verifyPhoneCode(NEW_PHONE, TEST_PHONE, TEST_CODE)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("zzzz", "onResponse: ${response.body()}")
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("zzzz", "onFailure: $call ====t:$t")
                }

                override fun onStart(call: Call<String>?) {
                    Log.e("zzzz", "onStart: $call")
                }

                override fun onCompleted(call: Call<String>?) {
                    Log.e("zzzz", "onCompleted: $call")
                }

            })
    }

    private fun verifyEmailCode() {
        loginApi.verifyEmailCode(NEW_EMAIL, TEST_EMAIL, TEST_CODE)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("zzzz", "onResponse: ${response.body()}")
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("zzzz", "onFailure: $call ====t:$t")
                }

                override fun onStart(call: Call<String>?) {
                    Log.e("zzzz", "onStart: $call")
                }

                override fun onCompleted(call: Call<String>?) {
                    Log.e("zzzz", "onCompleted: $call")
                }

            })
    }

    private fun checkPhone(): Boolean {
        if (TextUtils.isEmpty(binding.etLoginContent.text)) {
            ToastUtils.showMessage(this, getString(R.string.tip_phone_empty))
            return false
        }
        if (!RegexUtils.checkPhone(binding.etLoginContent.text.toString())) {
            ToastUtils.showMessage(this, getString(R.string.tip_phone_error))
            return false
        }
        return true
    }

    private fun checkEmail(): Boolean {
        if (TextUtils.isEmpty(binding.etLoginContent.text)) {
            ToastUtils.showMessage(this, getString(R.string.tip_email_empty))
            return false
        }
        if (!RegexUtils.checkEmail(binding.etLoginContent.text.toString())) {
            ToastUtils.showMessage(this, getString(R.string.tip_email_error))
            return false
        }
        return true
    }

    private fun login() {
        if (isPhone) {
            if (checkInfo()) {
                verifyPhoneCode()
            }
            return
        }
        if (checkInfo()) {
            verifyEmailCode()
        }
    }

    private fun checkInfo(): Boolean {
        if (isPhone) {
            if (checkPhone()) {
                if (binding.etVerifyCode.text?.length == 6) {
                    return true
                }
                ToastUtils.showMessage(this, getString(R.string.tip_code_error))
                return false
            }
            return false
        }
        if (checkEmail()) {
            if (binding.etVerifyCode.text?.length == 6) {
                return true
            }
            ToastUtils.showMessage(this, getString(R.string.tip_code_error))
            return false
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}