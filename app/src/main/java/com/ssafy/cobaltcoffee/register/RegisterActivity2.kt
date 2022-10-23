package com.ssafy.cobaltcoffee.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.ActivityRegister2Binding
import com.ssafy.cobaltcoffee.dto.User

private const val TAG = "RegisterActivity2_코발트"

class RegisterActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityRegister2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //이전 페이지에서 넘겨받은 유저 정보
        val user = intent.getSerializableExtra("userInfo") as User

        initTb() //툴바 적용

    }

    //툴바 적용하기
    private fun initTb() {
        binding.apply {
            setSupportActionBar(register2ToolBar.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            register2ToolBar.toolbarTitle.text = "회원가입"
        }
    }

    //뒤로가기 버튼 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
    }
}