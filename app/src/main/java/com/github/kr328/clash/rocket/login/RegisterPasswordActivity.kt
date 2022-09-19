package com.github.kr328.clash.rocket.login

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.github.kr328.clash.R
import com.github.kr328.clash.common.network.bean.EmailRegisterRequest
import com.github.kr328.clash.common.network.bean.PhoneRegisterRequest
import com.github.kr328.clash.common.network.bean.UserInfoData
import com.github.kr328.clash.common.network.http.RetrofitHelper
import com.github.kr328.clash.common.network.http.call.Callback
import com.github.kr328.clash.common.network.service.LoginApiService
import com.github.kr328.clash.common.util.Constants
import com.github.kr328.clash.common.util.Constants.Intent_Key.KEY_IS_PHONE
import com.github.kr328.clash.common.util.Constants.Intent_Key.KEY_USER_PHONE
import com.github.kr328.clash.design.databinding.ActivityRegisterPasswordBinding
import com.github.kr328.clash.design.util.ToastUtils
import com.github.kr328.clash.design.util.root
import retrofit2.Call
import retrofit2.Response

class RegisterPasswordActivity : AppCompatActivity() {
    companion object {
        fun startAction(activity: AppCompatActivity, account: String, isPhone: Boolean) {
            activity.startActivity(Intent(activity, RegisterPasswordActivity::class.java).apply {
                this.putExtra(KEY_USER_PHONE, account)
                this.putExtra(KEY_IS_PHONE, isPhone)
            })
        }
    }

    private lateinit var account: String
    private var isPhone = false
    private lateinit var loginApi: LoginApiService
    private lateinit var binding: ActivityRegisterPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPasswordBinding.inflate(layoutInflater, root, false)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        loginApi = RetrofitHelper.create(LoginApiService::class.java)
        account = intent.getStringExtra(KEY_USER_PHONE) ?: ""
        isPhone = intent.getBooleanExtra(KEY_IS_PHONE, false)
        binding.ivBack.setOnClickListener { finish() }
        binding.ivShowPassword.setOnClickListener {
            val selectionEnd = binding.etPassword.selectionEnd
            val selectionStart = binding.etPassword.selectionStart
            binding.ivShowPassword.isSelected = !binding.ivShowPassword.isSelected
            if (binding.ivShowPassword.isSelected) {
                binding.etPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

            } else {
                binding.etPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.etPassword.setSelection(selectionStart, selectionEnd)
        }
        binding.ivShowRepeat.setOnClickListener {
            val selectionEnd = binding.etRepeat.selectionEnd
            val selectionStart = binding.etRepeat.selectionStart
            binding.ivShowRepeat.isSelected = !binding.ivShowRepeat.isSelected
            if (binding.ivShowRepeat.isSelected) {
                binding.etRepeat.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.etRepeat.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.etRepeat.setSelection(selectionStart, selectionEnd);
        }
        binding.btnRegister.setOnClickListener {
            if (checkInfo()) {
                if (isPhone) {
                    registerByPhone()
                    return@setOnClickListener
                }
                registerByEmail()
            }
        }
    }

    private fun checkInfo(): Boolean {
        if (TextUtils.isEmpty(binding.etNickName.text.toString())) {
            ToastUtils.showMessage(this, getString(R.string.tip_nick_name_error))
            return false
        }
        if (TextUtils.isEmpty(binding.etPassword.text.toString()) || binding.etPassword.text.toString().length < 8) {
            ToastUtils.showMessage(this, getString(R.string.tip_password_less))
            return false
        }
        if (!TextUtils.equals(
                binding.etPassword.text.toString(),
                binding.etRepeat.text.toString()
            )
        ) {
            ToastUtils.showMessage(this, getString(R.string.tip_password_same))
            return false
        }
        return true
    }

    private fun registerByPhone() {
        loginApi.registerByPhone(
            PhoneRegisterRequest(
                account, binding.etNickName.text.toString(), binding.etPassword.text.toString()
            )
        ).enqueue(object : Callback<UserInfoData> {
            override fun onResponse(call: Call<UserInfoData>, response: Response<UserInfoData>) {

            }

            override fun onFailure(call: Call<UserInfoData>, t: Throwable) {
                Log.e("zzzz", "onFailure: $call ====t:$t")
            }

            override fun onStart(call: Call<UserInfoData>?) {
                Log.e("zzzz", "onStart: $call")
            }

            override fun onCompleted(call: Call<UserInfoData>?) {
                Log.e("zzzz", "onCompleted: $call")
            }

        })
    }

    private fun registerByEmail() {
        loginApi.registerByEmail(
            EmailRegisterRequest(
                account, binding.etNickName.text.toString(), binding.etPassword.text.toString()
            )
        ).enqueue(object : Callback<UserInfoData> {
            override fun onResponse(call: Call<UserInfoData>, response: Response<UserInfoData>) {

            }

            override fun onFailure(call: Call<UserInfoData>, t: Throwable) {
                Log.e("zzzz", "onFailure: $call ====t:$t")
            }

            override fun onStart(call: Call<UserInfoData>?) {
                Log.e("zzzz", "onStart: $call")
            }

            override fun onCompleted(call: Call<UserInfoData>?) {
                Log.e("zzzz", "onCompleted: $call")
            }

        })
    }
}