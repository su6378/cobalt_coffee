package com.ssafy.cobaltcoffee.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.cobaltcoffee.databinding.FragmentOrderBinding
import com.ssafy.cobaltcoffee.home.order.MenuFragment

class OrderFragment : Fragment() {
    private lateinit var homeActivity: HomeActivity
    private lateinit var binding: FragmentOrderBinding

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {


        binding.apply {
            viewPager.adapter = ViewPagerAdapter(parentFragmentManager, lifecycle)

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