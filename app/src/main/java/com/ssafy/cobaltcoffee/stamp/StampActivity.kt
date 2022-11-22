package com.ssafy.cobaltcoffee.stamp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.ActivityStampBinding
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel

private const val TAG = "StampActivity_코발트"
class StampActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStampBinding
    private val userViewModel : UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStampBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()


    }

    private fun init(){
        //이전 화면에서 user정보 받아오기
        userViewModel.currentUser = intent.getSerializableExtra("user") as User

        initTb()
        initTab()
    }

    //툴바 적용하기
    private fun initTb() {
        binding.apply {
            setSupportActionBar(stampToolBar.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            stampToolBar.toolbarTitle.text = "스탬프"
        }
    }

    //뒤로가기 버튼 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initTab(){
        // 탭 설정
        binding.stampTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // 탭이 선택 되었을 때
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 탭이 선택되지 않은 상태로 변경 되었을 때
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 이미 선택된 탭이 다시 선택 되었을 때
            }
        })

        // 뷰페이저에 어댑터 연결
        binding.stampVp.adapter = ViewPagerAdapter(this)

        /* 탭과 뷰페이저를 연결, 여기서 새로운 탭을 다시 만드므로 레이아웃에서 꾸미지말고
        여기서 꾸며야함
         */
        TabLayoutMediator(binding.stampTl, binding.stampVp) {tab, position ->
            when(position) {
                0 -> tab.text = "스탬프카드"
                1 -> tab.text = "스탬프적립내역"
            }
        }.attach()
    }

    inner class ViewPagerAdapter (fragment : FragmentActivity) : FragmentStateAdapter(fragment){
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> StampCardFragment()
                else -> StampHistoryFragment()
            }
        }
    }
}

