package com.ssafy.cobaltcoffee.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.cobaltcoffee.databinding.FragmentOtherBinding
import com.ssafy.cobaltcoffee.dialog.LogoutDialog

class OtherFragment : Fragment() {
    private lateinit var homeActivity: HomeActivity
    private lateinit var binding: FragmentOtherBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtherBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //로그아웃 버튼 클릭 시 dialog 생성
        binding.orderLogout.setOnClickListener {
            val dialog = LogoutDialog(requireActivity() as AppCompatActivity)
            dialog.setOnOKClickedListener {
                homeActivity.logout()
            }
            dialog.show("로그아웃 하시겠습니까?")
        }
    }

    private fun init() {


    }
}