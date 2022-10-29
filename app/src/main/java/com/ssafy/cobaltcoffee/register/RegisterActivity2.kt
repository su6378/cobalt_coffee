package com.ssafy.cobaltcoffee.register

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.ActivityRegister2Binding
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.service.UserService
import java.util.regex.Pattern

private const val TAG = "RegisterActivity2_코발트"

class RegisterActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityRegister2Binding

    //비밀번호
    private var passwordCheck = false
    //비밀번호 확인
    private var pwConfirmCheck = false
    //이름 확인
    private var nameCheck = false
    //닉네임 확인
    private var nicknameCheck = false

    //파이어베이스
    private lateinit var database: DatabaseReference

    //서비스
    private lateinit var userService: UserService
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as UserService.MyLocalBinder
            userService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //이전 페이지에서 넘겨받은 유저 정보
        val user = intent.getSerializableExtra("userInfo") as User

        initTb() //툴바 적용

        //비밀번호 체크
        binding.passwordEt.addTextChangedListener {
            checkPassword()
            registerCheck()
        }

        //비밀번호 확인 체크
        binding.passConfirmEt.addTextChangedListener {
            checkConfirm(binding.passConfirmEt.text?.trim().toString())
            registerCheck()
        }

        //이름
        binding.nameEt.addTextChangedListener {
            if (binding.nameEt.text.toString().isEmpty()) {
                nameCheck = false
                registerCheck()
            }
            else {
                nameCheck = true
                registerCheck()
            }
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
                user.name = nameEt.text?.trim().toString()
                user.nickname = nicknameEt.text?.trim().toString()

                //유저정보 삽입
                insert(user)

//                initDB()
//                database.child(user.name).setValue(user)
            }
        }
    }

    //onStart에서 binding
    override fun onStart() {
        super.onStart()
        val intent = Intent(this, UserService::class.java)

        //intent, 성공하면 callback?,
        bindService(intent, connection, BIND_AUTO_CREATE)
    }

    //onStop 에서 unbinding
    override fun onStop() {
        super.onStop()
        unbindService(connection)
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

    //파이어베이스 초기화
    private fun initDB(){
        database = Firebase.database.getReference("user")
    }

    //DB 삽입
    private fun insert(user : User){

        //서비스가 연결되어 있다면 삽입
        if (isBound){
            Log.d(TAG, "insert: 서비스 연결")
            userService.insert(user)
        }
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
        binding.registerBtn.isEnabled = passwordCheck && pwConfirmCheck && nameCheck && nicknameCheck
    }
}