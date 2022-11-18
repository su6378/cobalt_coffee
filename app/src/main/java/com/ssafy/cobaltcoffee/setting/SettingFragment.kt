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
import com.ssafy.cobaltcoffee.dialog.MarketingDialog
import com.ssafy.cobaltcoffee.start.StartActivity
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*

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
                showLogoutDialog()
            }
            //마케팅 활용 동의 클릭
            settingMarketingCl.setOnClickListener{
                showMarketingDialog()
            }
        }
    }

    //화면 초기화
    private fun init(){
        initTb()
        //아이디 초기화
        binding.settingId.text = userViewModel.currentUser.id
        //토글버튼 터치 불가능하게 만들기
        binding.settingLocationSb.isEnabled = false
        binding.settingMarketngSb.isEnabled = false
    }

    //툴바 적용하기
    private fun initTb() {
        binding.apply {
            val actionBar = settingActivity.actionBar
            actionBar?.setDisplayHomeAsUpEnabled(true)
            actionBar?.setDisplayShowTitleEnabled(false)
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            actionBar?.title = "설정"
        }
    }


    //뒤로가기 버튼 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                Log.d(TAG, "onOptionsItemSelected: 클릭됨!!")
                settingActivity.moveHomeActivity()
                settingActivity.onBackPressed()

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //로그아웃 다이얼로그 생성
    private fun showLogoutDialog(){
        val dialog = LogoutDialog(requireActivity() as AppCompatActivity)
        dialog.setOnOKClickedListener {
            settingActivity.logout()
        }
        dialog.show("로그아웃 하시겠습니까?")
    }

    //마케팅정보 동의 다이얼로그 생성
    private fun showMarketingDialog(){
        val dialog = MarketingDialog(requireActivity() as AppCompatActivity)
        dialog.setOnOKClickedListener {
            //변수 설정
            binding.settingMarketngSb.isChecked = !binding.settingMarketngSb.isChecked
            binding.settingMarketngSb.isEnabled = false
        }

        binding.settingMarketngSb.isEnabled = true
        val currentTime = getCurrentTime()
        if(!binding.settingMarketngSb.isChecked) { //동의를 한다면
            dialog.show("${currentTime}에\n${resources.getString(R.string.marketingAgree)}")
        }else{
            dialog.show("${currentTime}에\n${resources.getString(R.string.marketingDisagree)}")
        }

    }

    //현재 시간 가져오기
    fun getCurrentTime() : String{
        // 현재시간을 가져오기
        val long_now = System.currentTimeMillis()

        // 현재 시간을 Date 타입으로 변환
        val t_date = Date(long_now)

        // 날짜, 시간을 가져오고 싶은 형태 선언
        val t_dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 kk:mm", Locale("ko", "KR"))

        // 현재 시간을 dateFormat 에 선언한 형태의 String 으로 변환
        val str_date = t_dateFormat.format(t_date)
        return str_date
    }
}