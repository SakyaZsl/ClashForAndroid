package com.github.kr328.clash.rocket.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.kr328.clash.R
import com.github.kr328.clash.common.util.Constants.Intent_Key.KEY_IS_PHONE
import com.github.kr328.clash.common.util.Constants.Intent_Key.KEY_USER_PHONE
import com.github.kr328.clash.design.databinding.ActivityRegisterCodeBinding
import com.github.kr328.clash.design.util.root

class RegisterCodeActivity : AppCompatActivity() {
    private var isPhone = false
    private lateinit var account: String
    private lateinit var binding: ActivityRegisterCodeBinding

    companion object {
        fun startAction(activity: AppCompatActivity?, phone: String, isPhone: Boolean) {
            activity?.let {
                it.startActivity(Intent(it, RegisterCodeActivity::class.java).apply {
                    this.putExtra(KEY_USER_PHONE, phone)
                    this.putExtra(KEY_IS_PHONE, isPhone)
                })
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterCodeBinding.inflate(layoutInflater, root, false)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        isPhone = intent.getBooleanExtra(KEY_IS_PHONE, false)
        val bundle=intent.extras
        Log.e("zzzz", "initView: "+ intent.getStringExtra(KEY_USER_PHONE) )
        Log.e("zzzz", "initView2: "+ bundle?.getString(KEY_USER_PHONE) )
        account = intent.getStringExtra(KEY_USER_PHONE) ?: ""
        if (isPhone) {
            binding.tvRegisterCode.text = getString(R.string.register_code_phone,account)
        }else{
            binding.tvRegisterCode.text = getString(R.string.register_code_email,account)
        }
        binding.btnRegisterNext.setOnClickListener {
            RegisterPasswordActivity.startAction(this,account,isPhone)
        }
    }
}