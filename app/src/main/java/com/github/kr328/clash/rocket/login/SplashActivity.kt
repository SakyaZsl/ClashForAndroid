package com.github.kr328.clash.rocket.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.kr328.clash.MainActivity
import com.github.kr328.clash.R
import com.github.kr328.clash.common.network.http.RetrofitHelper
import com.github.kr328.clash.common.network.http.call.Callback
import com.github.kr328.clash.common.network.service.LoginApiService
import com.github.kr328.clash.common.network.service.MyApiService
import com.github.kr328.clash.common.util.Constants.Urls.MZSM_KEY
import retrofit2.Call
import retrofit2.Response

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        netWorkTest()
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            goHome()
        }, 3000)
    }

    private fun goHome() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun  netWorkTest(){
        RetrofitHelper.create(LoginApiService::class.java)
            .getArgument(MZSM_KEY).enqueue(object:Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("zzzz", "onResponse: $response" )
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("zzzz", "onFailure: $t" )
                }

                override fun onStart(call: Call<String>?) {
                    Log.e("zzzz", "onStart: $call" )
                }

                override fun onCompleted(call: Call<String>?) {
                    Log.e("zzzz", "onCompleted: $call" )
                }

            })
    }
}