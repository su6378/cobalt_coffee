package com.ssafy.cobaltcoffee.stamp

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.cobaltcoffee.adapter.OrderDetailAdapter
import com.ssafy.cobaltcoffee.adapter.StampCardAdapter
import com.ssafy.cobaltcoffee.databinding.FragmentStampCardBinding
import com.ssafy.cobaltcoffee.dto.UserLevel
import com.ssafy.cobaltcoffee.util.CommonUtils
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel


class StampCardFragment : Fragment() {
    private lateinit var binding: FragmentStampCardBinding
    private lateinit var stampActivity: StampActivity

    private lateinit var stampCardAdapter: StampCardAdapter

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        stampActivity = context as StampActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStampCardBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init(){
        binding.apply {

            //등급
            val grade = if(userViewModel.currentUser.stamps / 30 >= 4) 4 else userViewModel.currentUser.stamps / 30

            val count = userViewModel.currentUser.stamps - (30 * grade)
            stampCardStampCount.text = count.toString() //스탬프 적립 개수

            //쿠폰 받기까지 남은 스탬프 개수
            val leftCount = "${10 - (count % 10)}개"
            val content = "Free 쿠폰 발행까지 ${leftCount}의 스탬프가 남았습니다."
            stampCardCouponCount.text = CommonUtils.setSpannableText(leftCount,content)

            //닉네임과 등급 정보
            val level = UserLevel.userInfoList[grade].title
            val nickname = userViewModel.currentUser.name
            val userGrade = "${nickname}님은 ${level}입니다."
            stampCardNameGrade.text = CommonUtils.setSpannableText(level,userGrade)

            //등급업까지 남은 횟수
            val levelUp = "다음 등급업까지 ${30-count}개 남았습니다.\n (등급반영은 최대 24시간 소요)"
            stampCardLevelUp.text = levelUp

            //적립한 쿠폰 리사이클러뷰
            initAdapter(count)

        }
    }

    private fun initAdapter(stampCount : Int){
        val stampList = ArrayList<Int>()
        repeat(30){
            if (it < stampCount) stampList.add(1)
            else stampList.add(0)
        }

        stampCardAdapter = StampCardAdapter(stampList)

        binding.stampCardRv.apply {
            val gridLayoutManager = GridLayoutManager(context,5)
            layoutManager = gridLayoutManager
            adapter = stampCardAdapter
            //원래의 목록위치로 돌아오게함
            adapter!!.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }



}