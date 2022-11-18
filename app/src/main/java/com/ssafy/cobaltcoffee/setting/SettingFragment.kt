package com.ssafy.cobaltcoffee.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.databinding.FragmentSettingBinding
import com.ssafy.cobaltcoffee.dialog.LogoutDialog
import com.ssafy.cobaltcoffee.start.StartActivity
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel

private const val TAG = "SettingFragment_코발트"
class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var settingActivity: SettingActivity

    private val userViewModel : UserViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        settingActivity = context as SettingActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        binding.apply {

            //닉네임 변경 클릭
            nicknameChangeTv.setOnClickListener{

            }
            //비밀번호 변경 클릭
            passwordChangeTv.setOnClickListener {

            }
            //로그아웃 클릭
            logoutBtn.setOnClickListener {
                showDialog()
            }
        }
    }

    //화면 초기화
    private fun init(){
        initTb()
        //아이디 초기화
        Log.d(TAG, "아이디:${userViewModel.currentUser} ")
        binding.settingId.text = userViewModel.currentUser.id
    }

    //툴바 적용하기
    private fun initTb() {
        binding.apply {
            settingActivity.setSupportActionBar(settingToolBar.toolbar)
            settingActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            settingActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
            settingActivity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            settingToolBar.toolbarTitle.text = "설정"
        }
    }


    //뒤로가기 버튼 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                settingActivity.moveHomeActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //로그아웃 다이얼로그 생성
    private fun showDialog(){
        val dialog = LogoutDialog(requireActivity() as AppCompatActivity)
        dialog.setOnOKClickedListener {
            settingActivity.logout()
        }
        dialog.show("로그아웃 하시겠습니까?")
    }
}