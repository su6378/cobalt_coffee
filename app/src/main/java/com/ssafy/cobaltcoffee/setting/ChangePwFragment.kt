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
import com.ssafy.cobaltcoffee.databinding.FragmentChangePwBinding
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import java.util.regex.Pattern

private const val TAG = "ChangePwFragment_코발트"
class ChangePwFragment : Fragment() {

    private lateinit var binding: FragmentChangePwBinding
    private lateinit var settingActivity: SettingActivity

    //비밀번호
    private var passwordCheck = false

    //비밀번호 확인
    private var pwConfirmCheck = false

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
        binding = FragmentChangePwBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        binding.apply {
            //비밀번호 체크
            passwordEt.addTextChangedListener {
                checkPassword()
                //비밀번호 확인 입력칸이 비어있지않다면 체크 메소드 실행
                if (passConfirmEt.text!!.isNotEmpty()) {
                    checkConfirm(passConfirmEt.text?.trim().toString())
                }
                changeCheck()
            }

            //비밀번호 확인 체크
            passConfirmEt.addTextChangedListener {
                checkConfirm(passConfirmEt.text?.trim().toString())
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
        settingActivity.changeTitle("비밀번호 변경")
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
                    passwordTl.setHelperTextColor(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cobalt
                            )
                        )
                    )
                    passwordCheck = true
                    return
                }
            }
        }
    }

    //비밀번호 확인 메소드
    private fun checkConfirm(pwConfirm: String) {
        binding.apply {
            val password = passwordEt.text?.trim().toString()
            if (pwConfirm == password) {
                passConfirmTl.error = null
                passConfirmTl.helperText = "비밀번호가 일치합니다."
                passConfirmTl.setHelperTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.cobalt
                        )
                    )
                )
                pwConfirmCheck = true
            } else {
                passConfirmTl.error = "비밀번호가 일치하지 않습니다."
                pwConfirmCheck = false
            }
        }
    }

    //변경하기 버튼 활성화
    private fun changeCheck() {
        binding.changeBtn.isEnabled = passwordCheck && pwConfirmCheck
    }

    //회원정보 수정
    private fun updateUser() {
        userViewModel.currentUser.pw = binding.passwordEt.text.toString().trim()
        //유저정보 변경
        UserRepository.get().update(userViewModel.currentUser, UpdateCallback())
    }

    //회원수정 콜백
    inner class UpdateCallback : RetrofitCallback<Boolean> {
        override fun onSuccess(code: Int, result: Boolean) {
            if (result) {
                //snackbar
                val snack = Snackbar.make(binding.root, "비밀번호가 변경되었습니다.", Snackbar.LENGTH_SHORT)
                snack.setTextColor(Color.WHITE)    // 텍스트 컬러
                snack.setBackgroundTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.blue
                    )
                )    // 배경 컬러
                snack.animationMode = Snackbar.ANIMATION_MODE_FADE    // 애니메이션
                snack.show()
            } else {
                Snackbar.make(binding.root, "비밀번호 변경에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
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