package com.github.kr328.clash.rocket.mine

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.kr328.clash.R
import com.github.kr328.clash.common.network.http.RetrofitHelper
import com.github.kr328.clash.common.network.http.call.Callback
import com.github.kr328.clash.common.network.service.UserApiService
import com.github.kr328.clash.common.util.Constants.Urls.token
import com.github.kr328.clash.design.databinding.ActivityUserInfoBinding
import com.github.kr328.clash.design.store.UiStore
import com.github.kr328.clash.design.util.root
import retrofit2.Call
import retrofit2.Response

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserInfoBinding
    private lateinit var userApiService: UserApiService

    companion object {
        fun startAction(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, UserInfoActivity::class.java))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater, root, false)
        setContentView(binding.root)
        initView()
        getUserInfo()
    }

    private fun initView() {
        userApiService= RetrofitHelper.create(UserApiService::class.java)
        binding.ivClose.setOnClickListener { finish() }

    }

    fun  getUserInfo(){
        userApiService.getUserInfo(token).enqueue(object :Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.e("zzzz", "onResponse: ${response.body()}" )
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("zzzz", "onFailure t: $t}" )
            }

            override fun onStart(call: Call<String>?) {
                Log.e("zzzz", "onStart: $call}" )
            }

            override fun onCompleted(call: Call<String>?) {
                Log.e("zzzz", "onCompleted: $call}" )
            }

        })
    }


}