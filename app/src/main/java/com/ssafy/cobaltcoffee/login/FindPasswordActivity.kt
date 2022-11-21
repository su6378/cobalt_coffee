package com.ssafy.cobaltcoffee.login

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.ActivityFindPasswordBinding
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import java.util.regex.Pattern

private const val TAG = "FindPasswordActivity_코발트"

class FindPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFindPasswordBinding

    // 이메일 정규식
    val emailValidation =
        "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

    //이메일 패턴 체크
    private var emailCheck = false

    //존재하는 아이디인지 체크
    var checkedId = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTb()

        binding.apply {

            //이메일 찾기
            findPwEmailEt.addTextChangedListener {
                emailValidation()
            }

            //비밀번호 찾기 버튼 클릭
            findBtn.setOnClickListener {
                getUser(findPwEmailEt.text.toString().trim())
            }
        }
    }

    //툴바 적용하기
    private fun initTb() {
        binding.apply {
            setSupportActionBar(findPwToolBar.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            findPwToolBar.toolbarTitle.text = "비밀번호 찾기"
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

    //이메일 유효성 체크 메소드
    private fun emailValidation() {
        var email = binding.findPwEmailEt.text.toString().trim() //공백제거
        val e = Pattern.matches(emailValidation, email)
        //이메일이 비어있는 경우
        if (email.isEmpty()) {
            binding.findPwTl.error = "이메일을 입력하세요."
            emailCheck = false
            return
        } else {
            //이메일 형태가 정상일 경우
            if (e) {
                duplicateCheck(email)
            } else {
                binding.findPwTl.error = "이메일 형식이 올바르지 않습니다."
                emailCheck = false
            }
        }
        findPassword()
    }

    //비밀번호 찾기 버튼 활성
    private fun findPassword() {
        binding.findBtn.isEnabled = emailCheck == true
    }

    //아이디 사용 조회
    private fun duplicateCheck(id: String) {
        UserRepository.get().duplicateCheck(id, DuplicateCallback())
    }

    inner class DuplicateCallback : RetrofitCallback<Boolean> {
        override fun onSuccess(code: Int, result: Boolean) {
            checkedId = !result
            if (result) {
                binding.findPwTl.helperText = "가입된 이메일입니다."
                binding.findPwTl.setHelperTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@FindPasswordActivity,
                            R.color.cobalt
                        )
                    )
                )
                binding.findPwTl.error = null
                emailCheck = true
                findPassword()
            } else {
                binding.findPwTl.error = "가입되지 않은 이메일입니다."
                emailCheck = false
                findPassword()
            }
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "유저 정보 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }

    //아이디 사용 조회
    private fun getUser(id: String) {
        UserRepository.get().getInfo(id, GetUserInfoCallback())
    }

    inner class GetUserInfoCallback: RetrofitCallback<HashMap<String, Any>> {
        override fun onSuccess( code: Int, result: HashMap<String,Any>) {
            val jsonString = result

            val user : User = Gson().fromJson(jsonString["user"].toString(), object: TypeToken<User>(){}.type)

            binding.apply {
                findPw.text = "현재 사용중인 비밀번호: ${user.pw}"
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