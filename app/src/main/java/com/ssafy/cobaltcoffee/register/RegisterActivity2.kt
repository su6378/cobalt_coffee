package com.ssafy.cobaltcoffee.register

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.forms.sti.progresslitieigb.ProgressLoadingIGB
import com.forms.sti.progresslitieigb.finishLoadingIGB
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.databinding.ActivityRegister2Binding
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.home.HomeActivity
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import java.util.regex.Pattern


private const val TAG = "RegisterActivity2_코발트"

class RegisterActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityRegister2Binding
    private lateinit var user : User
    //비밀번호
    private var passwordCheck = false
    //비밀번호 확인
    private var pwConfirmCheck = false
    //닉네임 확인
    private var nicknameCheck = false

    //Firebase Auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        //이전 페이지에서 넘겨받은 유저 정보
        user = intent.getSerializableExtra("userInfo") as User
        initTb() //툴바 적용

        //비밀번호 체크
        binding.passwordEt.addTextChangedListener {
            checkPassword()
            //비밀번호 확인 입력칸이 비어있지않다면 체크 메소드 실행
            if (binding.passConfirmEt.text!!.isNotEmpty()){
                checkConfirm(binding.passConfirmEt.text?.trim().toString())
            }
            registerCheck()
        }
        //비밀번호 확인 체크
        binding.passConfirmEt.addTextChangedListener {
            checkConfirm(binding.passConfirmEt.text?.trim().toString())
            registerCheck()
        }
        //닉네임
        binding.nicknameEt.addTextChangedListener {
            if (binding.nicknameEt.text.toString().isEmpty()) {
                nicknameCheck = false
                registerCheck()
            }
            else {
                nicknameCheck = true
                registerCheck()
            }
        }
        //가입하기
        binding.apply {
            registerBtn.setOnClickListener {
                user.pw = passwordEt.text?.trim().toString()
                user.name = nicknameEt.text?.trim().toString()
                //유저정보 삽입
                registerUser()
            }
        }
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

    //회원정보 삽입
    private fun registerUser(){
        //로딩창 생성 Lottie
        ProgressLoadingIGB.startLoadingIGB(this){
            message = "잠시만 기다려주세요."
            srcLottieJson = R.raw.loading_register
            hight = 500 // Optional
            width = 500 // Optional
            sizeTextMessage = 14f
        }
        UserRepository.get().insert(user,RegisterCallback())
    }

    //비밀번호 유효성 체크 메소드
    private fun checkPassword() {
        binding.apply {
            var password = passwordEt.text.toString().trim()
            if (password.isEmpty()) {
                passwordTl.error = "비밀번호는 대문자, 소문자, 숫자, 특수문자 중 2종류 이상을 조합하여 8자리 이상 입력해주세요."
                passwordCheck = false
                return
            } else {
                if (!Pattern.matches(
                        "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", password
                    )
                ) {
                    //비밀번호 형태가 정상일 경우
                    passwordTl.error = "비밀번호는 대문자, 소문자, 숫자, 특수문자 중 2종류 이상을 조합하여 8자리 이상 입력해주세요."
                    passwordCheck = false
                    return
                } else {
                    passwordTl.error = null
                    passwordTl.helperText = "사용가능한 비밀번호입니다."
                    passwordTl.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(this@RegisterActivity2, R.color.cobalt)))
                    passwordCheck = true
                    return
                }
            }
        }
    }

    //비밀번호 확인 메소드
    private fun checkConfirm(pwConfirm : String){
        binding.apply {
            val password = passwordEt.text?.trim().toString()
            if (pwConfirm == password) {
                passConfirmTl.error = null
                passConfirmTl.helperText = "비밀번호가 일치합니다."
                passConfirmTl.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(this@RegisterActivity2, R.color.cobalt)))
                pwConfirmCheck = true
            }else{
                passConfirmTl.error = "비밀번호가 일치하지 않습니다."
                pwConfirmCheck = false
            }
        }

    }
    //가입하기 버튼 활성화
    private fun registerCheck(){
        binding.registerBtn.isEnabled = passwordCheck && pwConfirmCheck && nicknameCheck
    }

    //회원가입과 동시에 auth 로그인 처리하기
    private fun loginAuth(){
        auth.signInWithEmailAndPassword(user.id,user.pw)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    moveHome()
                }else{
                }
            }
    }

    //홈 화면으로 이동
    private fun moveHome(){
        // 로그인 시 user정보 sp에 저장
        ApplicationClass.sharedPreferencesUtil.addUser(user)

        //lottie animation 종료
        finishLoadingIGB()
        //홈 액티비티로 이동
        val intent = Intent(this@RegisterActivity2, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    //회원가입 콜백
    inner class RegisterCallback: RetrofitCallback<Boolean> {
        override fun onSuccess( code: Int, result: Boolean) {
            if (result) {
                //FireAuth에 계정 등록
                auth.createUserWithEmailAndPassword(user.id,user.pw)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            loginAuth()
                        }else{
                            val snack = Snackbar.make(binding.root, "이미 존재하는 계정입니다.",Snackbar.LENGTH_SHORT)
                            snack.setTextColor(Color.WHITE)	// 텍스트 컬러
                            snack.setBackgroundTint(ContextCompat.getColor(this@RegisterActivity2,R.color.blue))	// 배경 컬러
                            snack.animationMode = Snackbar.ANIMATION_MODE_FADE	// 애니메이션
                            snack.show()
                        }
                    }
            }else{
                Snackbar.make(binding.root,"회원가입에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
            }
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "서버 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }
}