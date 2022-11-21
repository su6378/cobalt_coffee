package com.ssafy.cobaltcoffee.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.databinding.ActivityLoginBinding
import com.ssafy.cobaltcoffee.dialog.LoginDialog
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.home.HomeActivity
import com.ssafy.cobaltcoffee.register.RegisterActivity
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.smartstore.util.RetrofitCallback
import java.util.regex.Pattern

private const val TAG = "LoginActivity_코발트"
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private var autoLoginChecked = false

    // 이메일 정규식
    val emailValidation =
        "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //이메일
        binding.loginEmailEt.addTextChangedListener {
            emailValidation()
        }

        binding.apply {
            //자동 로그인
            autoLoginCb.setOnClickListener{
                autoLoginChecked = autoLoginCb.isChecked
            }
            //로그인
            loginBtn.setOnClickListener {
                login(binding.loginEmailEt.text.toString().trim(),binding.loginPwEt.text.toString().trim())
            }
            //비밀번호 찾기 페이지 이동
            searchPwTv.setOnClickListener{
                moveFindPw()
            }
            //회원가입 페이지 이동
            registerTv.setOnClickListener {
                moveRegister()
            }
        }
    }

    // 로그인 기능
    private fun login(loginId: String, loginPass: String) {
        val user = User(loginId, loginPass)
        UserRepository.get().login(user, LoginCallback())
    }
    //홈으로 이동
    private fun moveHome(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //자동 로그인 체크 안할시 아이디 값 넘겨주기
        if (!autoLoginChecked){
            intent.putExtra("userId",binding.loginEmailEt.text.toString().trim())
        }
        startActivity(intent)
    }
    //비밀번호 찾기 페이지 이동
    private fun moveFindPw() = startActivity(Intent(this,FindPasswordActivity::class.java))

    //회원가입 페이지 이동
    private fun moveRegister() = startActivity(Intent(this,RegisterActivity::class.java))

    //로그인 콜백
    inner class LoginCallback: RetrofitCallback<User> {
        override fun onSuccess( code: Int, user: User) {
            if (user.id != null) {
                Toast.makeText(this@LoginActivity,"로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                // 자동 로그인 체크시 sp에 유저 정보 저장
                if (autoLoginChecked) ApplicationClass.sharedPreferencesUtil.addUser(user)
                moveHome()
            }else{
                Toast.makeText(this@LoginActivity,"아이디가 존재하지 않습니다.",Toast.LENGTH_SHORT).show()
            }
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "유저 정보 불러오는 중 통신오류")
            showLoginDialog()
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }

    //이메일 유효성 체크 메소드
    private fun emailValidation() {
        var email = binding.loginEmailEt.text.toString().trim() //공백제거
        val e = Pattern.matches(emailValidation, email)
        //이메일이 비어있는 경우
        if (email.isEmpty()) {
            binding.loginEmailTl.error = "이메일을 입력하세요."
            return
        } else {
            //이메일 형태가 정상일 경우
            if (e) {
                binding.loginEmailTl.error = null
            } else {
                binding.loginEmailTl.error = "이메일 형식이 올바르지 않습니다."
            }
        }
    }

    //로그인 실패 다이얼로그 생성
    private fun showLoginDialog(){
        val dialog = LoginDialog(this)
        dialog.setOnOKClickedListener {

        }
        dialog.show("계정정보가 일치하지 않습니다.")
    }


}