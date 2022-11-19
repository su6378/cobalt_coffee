package com.ssafy.cobaltcoffee.setting

import android.app.Notification
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.databinding.FragmentSettingBinding
import com.ssafy.cobaltcoffee.dialog.LogoutDialog
import com.ssafy.cobaltcoffee.dialog.MarketingDialog
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.cobaltcoffee.start.StartActivity
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel
import com.ssafy.smartstore.util.RetrofitCallback
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //프래그먼트에서 툴바사용시 등록
        setHasOptionsMenu(true)
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

            //닉네임 변경 페이지 이동
            nicknameChangeTv.setOnClickListener{
                settingActivity.openFragment(1)
            }
            //비밀번호 변경 페이지 이동
            passwordChangeTv.setOnClickListener {
                settingActivity.openFragment(2)
            }
            //로그아웃 클릭
            logoutBtn.setOnClickListener {
                showLogoutDialog()
            }
            //푸쉬알림 클릭
            settingPushCl.setOnClickListener{
                presentNotificationSetting(requireContext())
            }
            //마케팅 활용 동의 클릭
            settingMarketingCl.setOnClickListener{
                showMarketingDialog()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUser()
    }



    //화면 초기화
    private fun init(){
        //툴바 타이틀 변경
        settingActivity.changeTitle("설정")
        //사용자 아이디 초기화
        binding.settingId.text = userViewModel.currentUser.id
        //사용자 정보 초기화
        getUser(userViewModel.currentUser.id)
    }


    //뒤로가기 버튼 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
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

    //버전별 푸쉬알림 설정창으로 이동
    fun presentNotificationSetting(context: Context) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationSettingOreo(context)
        } else {
            notificationSettingOreoLess(context)
        }
        try {
            context.startActivity(intent)
        }catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun notificationSettingOreo(context: Context): Intent {
        return Intent().also { intent ->
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    fun notificationSettingOreoLess(context: Context): Intent {
        return Intent().also { intent ->
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", context.packageName)
            intent.putExtra("app_uid", context.applicationInfo?.uid)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    //마케팅정보 동의 다이얼로그 생성
    private fun showMarketingDialog(){
        val dialog = MarketingDialog(requireActivity() as AppCompatActivity)
        dialog.setOnOKClickedListener {
            //변수 설정
            userViewModel.currentUser.isMarketing = !binding.settingMarketngSb.isChecked
            updateUser()
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

    //사용자 조회
    private fun getUser(id: String) {
        UserRepository.get().getInfo(id, GetUserInfoCallback())
    }

    inner class GetUserInfoCallback: RetrofitCallback<HashMap<String, Any>> {
        override fun onSuccess( code: Int, result: HashMap<String,Any>) {
            val jsonString = result

            userViewModel.currentUser = Gson().fromJson(jsonString["user"].toString(), object: TypeToken<User>(){}.type)

            binding.apply {
                Log.d(TAG, "onSuccess: ${userViewModel.currentUser.isPush}")
                settingPushSb.isChecked = userViewModel.currentUser.isPush
                settingLocationSb.isChecked = userViewModel.currentUser.isLocation
                settingMarketngSb.isChecked = userViewModel.currentUser.isMarketing
                //토글버튼 터치 불가능하게 만들기
                settingPushSb.isEnabled = false
                settingLocationSb.isEnabled = false
                settingMarketngSb.isEnabled = false
            }
        }
        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "유저 정보 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }

    //회원정보 수정
    private fun updateUser() {
        //푸쉬 알림
        userViewModel.currentUser.isPush = NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()
        //토글버튼 터치 불가능하게 만들기
        binding.apply {
            settingPushSb.isEnabled = true
            settingLocationSb.isEnabled = true
            settingMarketngSb.isEnabled = true
        }
        //유저정보 변경
        UserRepository.get().update(userViewModel.currentUser, UpdateCallback())
    }

    //회원수정 콜백
    inner class UpdateCallback : RetrofitCallback<Boolean> {
        override fun onSuccess(code: Int, result: Boolean) {
            if (result) {
                init()
            } else {
                Log.d(TAG, "회원정보 수정 실패 ")
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