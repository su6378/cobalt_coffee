package com.ssafy.cobaltcoffee.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.ActivityStartBinding
import com.ssafy.cobaltcoffee.login.LoginActivity
import com.ssafy.cobaltcoffee.register.RegisterActivity

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //회원가입 페이지로 이동
        binding.registerBtn.setOnClickListener {
            val intent = Intent(this@StartActivity, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        //로그인 페이지로 이동
        binding.loginBtn.setOnClickListener {
            val intent = Intent(this@StartActivity, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

    }
}