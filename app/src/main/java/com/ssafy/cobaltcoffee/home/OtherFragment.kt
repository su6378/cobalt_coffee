package com.ssafy.cobaltcoffee.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.databinding.FragmentOtherBinding
import com.ssafy.cobaltcoffee.dialog.LogoutDialog
import com.ssafy.cobaltcoffee.dto.CouponDetail
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.dto.UserLevel
import com.ssafy.cobaltcoffee.repository.CouponRepository
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel
import com.ssafy.cobaltcoffee.util.RetrofitCallback
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
            //장바구니 페이지로 이동
            otherBasketCl.setOnClickListener {
                homeActivity.cartPage(userViewModel.currentUser)
            }
            //주문내역 페이지로 이동
            otherHistoryCl.setOnClickListener {
                homeActivity.orderHistoryPage()
            }
            //스탬프 페이지로 이동
            stampPageBtn.setOnClickListener {
                homeActivity.stampPage(userViewModel.currentUser)
            }
            //쿠폰 페이지로 이동
            couponPageBtn.setOnClickListener {
                homeActivity.couponPage(userViewModel.currentUser)
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
        initUserCoupon()
    }

    //sp에 저장 되어있는 로그인 유저의 id로 retrofit userinfo 실행
    private fun getUserInfo(){
        if (userViewModel.userId.isEmpty()){
            val user = ApplicationClass.sharedPreferencesUtil.getUser()
            UserRepository.get().getInfo(user.id,GetUserInfoCallback())
        }else{
            UserRepository.get().getInfo(userViewModel.userId,GetUserInfoCallback())
        }
    }

    //유저 정보 초기화
    private fun initUser(){
        binding.apply {

            otherNickname.text = userViewModel.currentUser.name //닉네임

            //등급
            val grade = if(userViewModel.currentUser.stamps / 30 >= 4) 4 else userViewModel.currentUser.stamps / 30
            otherGrade.text = UserLevel.userInfoList[grade].title //현재 등급
            val color = resources.getIdentifier(UserLevel.userInfoList[grade].color,"color",requireContext().packageName)
            otherGrade.setTextColor(ContextCompat.getColor(requireContext(),color))
            otherUnitCount.text = "/${UserLevel.userInfoList[grade].max}" //max Count

            val count = userViewModel.currentUser.stamps - (30 * grade)
            if(count == 0) otherStampCount.text = "0"
            else{
                CoroutineScope(Dispatchers.Main).launch{
                    countUpStamp(count)
                }
            }
        }
    }

    //사용자의 쿠폰 정보를 가져옴
    private fun initUserCoupon() {
        CouponRepository.get().getCouponListCanUse(userViewModel.currentUser.id, CouponListCallback())
    }

    //카운트숫자를 점점 증가시켜서 화면에 보여줌
    private suspend fun countUpStamp(count: Int) {
        var current = 0
        while (current < count) {
            current++
            binding.otherStampCount.text = current.toString()
            delay(100)
        }
    }

    private suspend fun countUpCoupon(count: Int) {
        var current = 0
        while (current < count) {
            current++
            binding.otherCouponCount.text = current.toString()
            delay(100)
        }
    }

    inner class GetUserInfoCallback: RetrofitCallback<HashMap<String, Any>> {
        override fun onSuccess( code: Int, result: HashMap<String,Any>) {
            val jsonString = result
            userViewModel.currentUser = Gson().fromJson(jsonString["user"].toString(), object: TypeToken<User>(){}.type)
            initUser()
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "유저 정보 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }

    inner class CouponListCallback: RetrofitCallback<List<CouponDetail>> {
        override fun onSuccess(code: Int, result: List<CouponDetail>) {
            val couponList = result as MutableList<CouponDetail>
            var couponCnt = couponList.size

            CoroutineScope(Dispatchers.Main).launch{
                countUpCoupon(couponCnt)
            }
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "쿠폰 정보 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }
}