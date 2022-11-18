package com.ssafy.cobaltcoffee.home

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.databinding.FragmentOtherBinding
import com.ssafy.cobaltcoffee.dialog.LogoutDialog
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel
import com.ssafy.smartstore.util.RetrofitCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "OtherFragment_코발트"
class OtherFragment : Fragment() {
    private lateinit var homeActivity: HomeActivity
    private lateinit var binding: FragmentOtherBinding

    private val userViewModel : UserViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        binding.apply {
            //설정 버튼 클릭 시
            otherSettingBtn.setOnClickListener {
                homeActivity.moveSettingPage()
            }

            //로그아웃 버튼 클릭 시 dialog 생성
            otherLogout.setOnClickListener {
                showDialog()
            }
        }


    }

    override fun onResume() {
        super.onResume()
        init()
    }

    //로그아웃 다이얼로그 생성
    private fun showDialog(){
        val dialog = LogoutDialog(requireActivity() as AppCompatActivity)
        dialog.setOnOKClickedListener {
            homeActivity.logout()
        }
        dialog.show("로그아웃 하시겠습니까?")
    }

    private fun init() {
        getUserInfo()
        initUser()
    }

    //sp에 저장 되어있는 로그인 유저의 id로 retrofit userinfo 실행
    private fun getUserInfo(){
        val user = ApplicationClass.sharedPreferencesUtil.getUser()
        UserRepository.get().getInfo(user.id,GetUserInfoCallback())
    }

    //유저 정보 초기화
    private fun initUser(){
        binding.apply {
            otherNickname.text = userViewModel.currentUser.name
            CoroutineScope(Dispatchers.Main).launch{
                countUp(userViewModel.currentUser.stamps,"stamp")
            }
            otherStampCount.text = userViewModel.currentUser.stamps.toString()
        }
    }

    //카운트숫자를 점점 증가시켜서 화면에 보여줌
    private suspend fun countUp(count : Int, key : String) {
        var current = 0
        when(key){
            "stamp" -> {
                while (current < count) {
                    current++
                    binding.otherStampCount.text = current.toString()
                    delay(100)
                }
            }
            "coupon" ->{
                while (current <= count) {
                    current++
//                    binding.otherCouponCount.text = userViewModel.currentUser.stamps.toString()
                    delay(100)
                }
            }
        }

    }


    inner class GetUserInfoCallback: RetrofitCallback<HashMap<String,Any>> {
        override fun onSuccess( code: Int, result: HashMap<String,Any>) {
            val jsonString = result

            userViewModel.currentUser = Gson().fromJson(jsonString["user"].toString(), object: TypeToken<User>(){}.type)
//            val gradeInfo = Gson().fromJson<Grade>(jsonString["grade"].toString(), object: TypeToken<Grade>(){}.type)

            initUser()
            binding.apply {

//                val step = if (gradeInfo.step == 0) 1 else gradeInfo.step
//                textUserLevel.text = "${gradeInfo.title} ${step}단계"
//                textLevelRest.text = "다음 레벨까지 ${gradeInfo.to}잔 남았습니다."
//                Glide.with(this@MypageFragment)
//                    .load("${ApplicationClass.GRADE_URL}${gradeInfo.img}")
//                    .into(binding.imageLevel)
//                val currentProgress = UserLevel.userInfoList[step-1].unit - gradeInfo.to
//                proBarUserLevel.progress = currentProgress
//                textUserNextLevel.text = "${currentProgress}/${UserLevel.userInfoList[step-1].unit}"
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