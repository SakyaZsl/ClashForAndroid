package com.github.kr328.clash.rocket.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.kr328.clash.R
import com.github.kr328.clash.common.network.http.RetrofitHelper
import com.github.kr328.clash.common.network.http.call.Callback
import com.github.kr328.clash.common.network.service.LoginApiService
import com.github.kr328.clash.common.util.Constants
import com.github.kr328.clash.common.util.Constants.Email.NEW_EMAIL
import com.github.kr328.clash.design.databinding.ActivityRegisterBinding
import com.github.kr328.clash.design.util.RegexUtils
import com.github.kr328.clash.design.util.ToastUtils
import com.github.kr328.clash.design.util.root
import retrofit2.Call
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var isPhone = true
    private lateinit var loginApi: LoginApiService

    companion object {
        fun startAction(activity: AppCompatActivity?) {
            activity?.let {
                it.startActivity(Intent(it, RegisterActivity::class.java))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater, root, false)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        loginApi = RetrofitHelper.create(LoginApiService::class.java)
        binding.llRegisterPhone.setOnClickListener {
            isPhone = true
            changeLayoutByType()
        }
        binding.llRegisterEmail.setOnClickListener {
            isPhone = false
            changeLayoutByType()
        }
        binding.btnRegisterNext.setOnClickListener {
            if (isPhone) {
                if (checkPhone()) {
                    sendPhoneCode()
                }
                return@setOnClickListener
            }
            if (checkEmail()) {
                sendEmailCode()
            }
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun changeLayoutByType() {
        if (isPhone) {
            binding.tvPhonePre.visibility = View.VISIBLE
            binding.linePhone.visibility = View.VISIBLE
            binding.lineEmail.visibility = View.INVISIBLE
            return
        }
        binding.tvPhonePre.visibility = View.GONE
        binding.linePhone.visibility = View.INVISIBLE
        binding.lineEmail.visibility = View.VISIBLE
    }

    private fun sendPhoneCode() {
        loginApi.sendPhoneCode(
            Constants.Phone.NEW_PHONE,
            binding.etLoginContent.text.toString()
        ).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                RegisterCodeActivity.startAction(
                    this@RegisterActivity, binding.etLoginContent.text.toString(), isPhone
                )
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


    private fun sendEmailCode() {
        Log.e("zzzz", "sendEmailCode:")
        loginApi.sendEmailCode(NEW_EMAIL, binding.etLoginContent.text.toString())
            .enqueue(object : Callback<Any?> {
                override fun onResponse(call: Call<Any?>, response: Response<Any?>) {
                    Log.e("zzzz", "onResponse: ${response.body()}")
                    RegisterCodeActivity.startAction(this@RegisterActivity
                        ,binding.etLoginContent.text.toString(),false)
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


}