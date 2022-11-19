package com.ssafy.cobaltcoffee.setting

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.FragmentChangeNameBinding
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel
import com.ssafy.smartstore.util.RetrofitCallback

private const val TAG = "ChangeNameFragment_코발트"
class ChangeNameFragment : Fragment() {
    private lateinit var binding: FragmentChangeNameBinding
    private lateinit var settingActivity: SettingActivity

    //닉네임
    private var nicknameCheck = false

    private val userViewModel: UserViewModel by activityViewModels()

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
        binding = FragmentChangeNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        binding.apply {

            //닉네임 확인 체크
            nicknameEt.addTextChangedListener {
                checkNickname()
                changeCheck()
            }

            //변경하기
            changeBtn.setOnClickListener {
                updateUser()
            }
        }
    }

    //화면 초기화
    private fun init() {
        //툴바 타이틀 변경
        settingActivity.changeTitle("닉네임 변경")

        binding.apply {
            nicknameEt.hint = userViewModel.currentUser.name
        }
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

    //닉네임 확인 메소드
    private fun checkNickname() {
        binding.apply {
            val nickname = nicknameEt.text.toString().trim()
            if (nickname == userViewModel.currentUser.name) {
                nicknameTl.error = "현재 사용하고 계신 닉네임입니다."
                nicknameCheck = false
            }else if(nickname.isEmpty()){
                nicknameTl.error = "닉네임을 입력해주세요."
                nicknameCheck = false
            } else {
                nicknameTl.error = null
                nicknameCheck = true
                nicknameTl.helperText = "사용가능한 닉네임입니다."
                nicknameTl.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.cobalt)))
            }
        }
    }

    //변경하기 버튼 활성화
    private fun changeCheck() {
        binding.changeBtn.isEnabled = nicknameCheck
    }

    //회원정보 수정
    private fun updateUser(){
        userViewModel.currentUser.name = binding.nicknameEt.text.toString().trim()
        //유저정보 변경
        UserRepository.get().update(userViewModel.currentUser,UpdateCallback())
    }

    //회원수정 콜백
    inner class UpdateCallback: RetrofitCallback<Boolean> {
        override fun onSuccess( code: Int, result: Boolean) {
            if (result) {
                //snackbar
                val snack = Snackbar.make(binding.root, "닉네임이 변경되었습니다.",Snackbar.LENGTH_SHORT)
                snack.setTextColor(Color.WHITE)	// 텍스트 컬러
                snack.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.blue))	// 배경 컬러
                snack.animationMode = Snackbar.ANIMATION_MODE_FADE	// 애니메이션
                snack.show()
            }else{
                Snackbar.make(binding.root,"닉네임 변경에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
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