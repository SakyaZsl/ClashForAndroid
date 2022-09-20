package com.github.kr328.clash.rocket.mine

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.kr328.clash.design.databinding.ActivityAboutUsBinding
import com.github.kr328.clash.design.util.root

class AboutUsActivity : AppCompatActivity() {

    companion object {
        fun startAction(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, AboutUsActivity::class.java))
        }
    }

    private lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater, root, false)
        setContentView(binding.root)
    }

    fun initView() {

    }
}