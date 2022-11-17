package com.ssafy.cobaltcoffee.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.ActivityHomeBinding

const val HOME_FRAGMENT = 0
const val ORDER_FRAGMENT = 1
const val OTHER_FRAGMENT = 2

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        InitializeLayout()  // UI 드로우
        setFunction()       // 기능 설정
    }

    private fun InitializeLayout() {
        initTb()
        initNavigation()
    }

    // 툴바 적용
    private fun initTb() {
        binding.apply {
            setSupportActionBar(registerToolBar.toolbar)
            //supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            //supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            registerToolBar.toolbarTitle.text = "코발트 커피"
        }
    }

    private fun initNavigation() {
        binding.apply {
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_frame, HomeFragment()).commit()
            bottomNavigationView.setOnItemSelectedListener  {
                navigationSelected(it)
            }
        }
    }

    private fun navigationSelected(item: MenuItem): Boolean {
        binding.apply {
            val checked = item.setChecked(true)
            when (checked.itemId) {
                R.id.home_fragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_frame, HomeFragment()).commit()
                    true
                }
                R.id.order_fragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_frame, OrderFragment()).commit()
                    true
                }
                R.id.other_fragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_frame, OtherFragment()).commit()
                    true
                }
            }
            return false
        }
    }

    private fun setFunction() {

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}