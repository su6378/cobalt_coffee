package com.ssafy.cobaltcoffee.splash

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.ActivitySplashBinding
import com.ssafy.cobaltcoffee.start.StartActivity


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

    //다음 화면으로 이동
    private fun moveToNext(){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, StartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up_enter,R.anim.slide_up_exit)
            finish()
        },3000L) //3초 동안 지속
    }
}