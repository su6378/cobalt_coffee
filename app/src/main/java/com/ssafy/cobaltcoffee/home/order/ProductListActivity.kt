package com.ssafy.cobaltcoffee.home.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.cobaltcoffee.databinding.ActivityProductListBinding
import com.ssafy.cobaltcoffee.home.OrderFragment

class ProductListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductListBinding

    private val tabTitleArray = arrayOf(
        "메뉴1",
        "메뉴2",
        "메뉴3",
        "메뉴4",
        "메뉴5",
        "메뉴6",
        "메뉴7",
        "메뉴8"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.apply {
            viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitleArray[position]
            }.attach()
        }
    }

    inner class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int = tabTitleArray.size
        override fun createFragment(position: Int): Fragment {
            return MenuFragment(position)
        }
    }
}