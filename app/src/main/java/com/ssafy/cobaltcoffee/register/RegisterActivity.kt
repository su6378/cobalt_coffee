package com.ssafy.cobaltcoffee.register

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.ActivityRegisterBinding
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.cobaltcoffee.service.UserService
import com.ssafy.smartstore.util.RetrofitCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

private const val TAG = "RegisterActivity_코발트"

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    // 이메일 정규식
    val emailValidation =
        "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

    //체크해야하는 항목들
    private var emailCheck = false
    private var cobaltCheck = false
    private var personalInfoCheck = false
    private var locationCheck = false
    private var marketingCheck = false
    private var adCheck = false

    //아이디 중복체크
    var checkedId = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTb() //툴바 적용

        //이메일
        //firebaseAuth = FirebaseAuth.getInstance()
        binding.registerEmailEt.addTextChangedListener {
            emailValidation()
            nextPage()
        }

        initCbTitle() //체크박스 타이틀 및 내용 적용

        //전체 동의
        binding.registerAllCb.setOnClickListener {
            if (binding.registerAllCb.isChecked == true) allCheck(true)
            else allCheck(false)
            nextPage()
        }

        //각 항목 체크 시
        binding.apply {
            cobaltLinear.checkbox.setOnClickListener {
                cobaltCheck = cobaltLinear.checkbox.isChecked
                nextPage()
                isAllCheck()
            }
            personalInfoLinear.checkbox.setOnClickListener {
                personalInfoCheck = personalInfoLinear.checkbox.isChecked
                nextPage()
                isAllCheck()
            }
            locationLinear.checkbox.setOnClickListener {
                locationCheck = locationLinear.checkbox.isChecked
                nextPage()
                isAllCheck()
            }
            marketingLinear.checkbox.setOnClickListener {
                marketingCheck = marketingLinear.checkbox.isChecked
                nextPage()
                isAllCheck()
            }
            adLinear.checkbox.setOnClickListener {
                adCheck = adLinear.checkbox.isChecked
                nextPage()
                isAllCheck()
            }
        }

        //다음 페이지
        binding.registerNextBtn.setOnClickListener {
            val intent = Intent(this@RegisterActivity, RegisterActivity2::class.java)
            val email = binding.registerEmailEt.text.toString().trim()
            intent.putExtra("userInfo", User(email, "", "", 0))
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_none)
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

    //유저 조회
    private fun duplicateCheck(id: String){
        UserRepository.get().duplicateCheck(id,DuplicateCallback())
    }

    //이메일 유효성 체크 메소드
    private fun emailValidation() {
        var email = binding.registerEmailEt.text.toString().trim() //공백제거
        val e = Pattern.matches(emailValidation, email)
//        Log.d(TAG, "이메일: $email")
        //이메일이 비어있는 경우
        if (email.isEmpty()) {
            binding.registerTl.error = "이메일을 입력하세요."
            emailCheck = false
            return
        } else {
            //이메일 형태가 정상일 경우
            if (e) {
                duplicateCheck(email)
            } else {
                binding.registerTl.error = "이메일 형식이 올바르지 않습니다."
                emailCheck = false
            }
        }
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
    private fun allCheck(check: Boolean) {
        binding.apply {
            cobaltLinear.checkbox.isChecked = check
            personalInfoLinear.checkbox.isChecked = check
            locationLinear.checkbox.isChecked = check
            marketingLinear.checkbox.isChecked = check
            adLinear.checkbox.isChecked = check
        }
    }

    //전체 체크가 되어있다면 전체 동의 체크박스 체크 표시
    private fun isAllCheck() {
        binding.registerAllCb.isChecked =
            cobaltCheck == true && personalInfoCheck == true && locationCheck == true && marketingCheck == true && adCheck == true
    }

    //약관 부분 동의되어 있는 항목 boolean 값 갱신 및 필수 항목 체크 여부 확인
    private fun checkUserAgree(): Boolean {
        //체크항목 갱신하기
        cobaltCheck = binding.cobaltLinear.checkbox.isChecked
        personalInfoCheck = binding.personalInfoLinear.checkbox.isChecked
        locationCheck = binding.locationLinear.checkbox.isChecked
        marketingCheck = binding.marketingLinear.checkbox.isChecked
        adCheck = binding.adLinear.checkbox.isChecked

        //필수항목 체크 여부 확인
        return cobaltCheck && personalInfoCheck
    }

    //다음 페이지로 넘어갈 수 있는지 확인
    private fun nextPage() {
        binding.registerNextBtn.isEnabled = checkUserAgree() && emailCheck == true
    }

    inner class DuplicateCallback: RetrofitCallback<Boolean> {
        override fun onSuccess( code: Int, result: Boolean) {
            checkedId = !result
            if (result) {
                binding.registerTl.error = "이미 사용중인 이메일입니다."
                emailCheck = false
            }else{
                binding.registerTl.error = null
                binding.registerTl.helperText = "사용가능한 이메일 입니다."
                binding.registerTl.setHelperTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(this@RegisterActivity, R.color.cobalt)
                    )
                )
                emailCheck = true
            }
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "유저 정보 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }
}