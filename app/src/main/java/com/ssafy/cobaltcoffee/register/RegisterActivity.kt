package com.ssafy.cobaltcoffee.register

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTb() //툴바 적용

        initCbTitle() //체크박스 타이틀 및 내용 적용

        //전체 동의
        binding.registerAllCb.setOnClickListener {
            if (binding.registerAllCb.isChecked == true) allCheck(true)
            else allCheck(false)
        }
    }

    //툴바 적용하기
    private fun initTb() {
        binding.apply {
            setSupportActionBar(registerToolBar.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            registerToolBar.toolbarTitle.text = "회원가입"
        }
    }

    //뒤로가기 버튼 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(0, 0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //체크박스 약관 타이틀 적용
    private fun initCbTitle() {
        binding.cobaltLinear.checkbox.text = "코발트커피 서비스 이용약관"
        binding.personalInfoLinear.checkbox.text = "개인정보 수집 및 이용약관"
        binding.locationLinear.checkbox.text = "위치기반 서비스 이용약관"
        binding.locationLinear.checkboxTv.text = "(선택)"
        binding.locationLinear.checkboxTv.setTextColor(Color.RED)
        binding.marketingLinear.checkbox.text = "마케팅 활용 동의"
        binding.marketingLinear.checkboxTv.text = "(선택)"
        binding.marketingLinear.checkboxTv.setTextColor(Color.RED)
        binding.adLinear.checkbox.text = "광고성 정보 수신동의"
        binding.adLinear.checkboxTv.text = "(선택)"
        binding.adLinear.checkboxTv.setTextColor(Color.RED)
    }

    //전체 동의 버튼 클릭 시
    private fun allCheck(check : Boolean) {
        binding.apply {
            cobaltLinear.checkbox.isChecked = check
            personalInfoLinear.checkbox.isChecked = check
            locationLinear.checkbox.isChecked = check
            marketingLinear.checkbox.isChecked = check
            adLinear.checkbox.isChecked = check
        }
    }
}