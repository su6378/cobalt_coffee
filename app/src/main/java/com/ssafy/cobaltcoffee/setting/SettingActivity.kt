package com.ssafy.cobaltcoffee.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.databinding.ActivitySettingBinding
import com.ssafy.cobaltcoffee.dialog.LogoutDialog
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.home.HomeActivity
import com.ssafy.cobaltcoffee.start.StartActivity
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel

private const val TAG = "SettingActivity_코발트"

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var userData: User
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()


    }

    private fun init() {
        //이전 액티비티에서 데이터받아오기
        userData = intent.getSerializableExtra("user") as User

        //현재 사용자 초기화
        userViewModel.currentUser = userData

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_setting, SettingFragment())
            .commit()

        initTb()
    }

    //툴바 적용하기
    private fun initTb() {
        binding.apply {
            setSupportActionBar(settingToolBar.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    fun openFragment(index: Int, key: String, value: Int) {
        moveFragment(index, key, value)
    }

    fun openFragment(index: Int) {
        moveFragment(index, "", 0)
    }

    private fun moveFragment(index: Int, key: String, value: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        when (index) {
            0 -> transaction.replace(R.id.frame_layout_setting, SettingFragment())
                .addToBackStack(null)
            //닉네임 변경
            1 -> transaction.replace(R.id.frame_layout_setting, ChangeNameFragment())
                .addToBackStack(null)
            //비밀번호 변경
            2 -> transaction.replace(R.id.frame_layout_setting, ChangePwFragment())
                .addToBackStack(null)
            //로그아웃
            3 -> logout()
        }
        transaction.commit()
    }

    fun logout() {
        //preference 지우기
        ApplicationClass.sharedPreferencesUtil.deleteUser()
        //화면이동
        val intent = Intent(this, StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
    }


    //툴바 타이틀 변경
    fun changeTitle(title: String) {
        binding.settingToolBar.toolbarTitle.text = title
    }

}