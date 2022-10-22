package com.ssafy.cobaltcoffee.splash

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.ssafy.cobaltcoffee.MainActivity
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //상태바 투명하게 만들기
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val wic = WindowCompat.getInsetsController(window, window.decorView)
        wic.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
        wic.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.TRANSPARENT

        moveToNext()
    }

    //다음 스플래시 화면으로 이동
    private fun moveToNext(){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
            finish()
        },3000L) //3초 동안 지속
    }
}