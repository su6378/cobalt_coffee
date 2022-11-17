package com.ssafy.cobaltcoffee.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.databinding.ActivityLoginBinding
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.home.HomeActivity
import com.ssafy.cobaltcoffee.register.RegisterActivity
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.smartstore.util.RetrofitCallback

private const val TAG = "LoginActivity_코발트"
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private var autoLoginChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        Log.d(TAG, "user: $user $loginId $loginPass")
        UserRepository.get().login(user, LoginCallback())
    }
    //홈으로 이동
    private fun moveHome(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
    }
    //비밀번호 찾기 페이지 이동
//    private fun movePw() = startActivity(Intent(this,RegisterActivity::class.java))

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
                Toast.makeText(this@LoginActivity,"ID 또는 패스워드를 확인해 주세요.", Toast.LENGTH_SHORT).show()
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