package com.ssafy.cobaltcoffee.coupon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.ssafy.cobaltcoffee.databinding.ActivityCouponBinding
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel

private const val TAG = "CouponActivity_코발트"
class CouponActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCouponBinding
    private val userViewModel : UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCouponBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

    }
}