package com.ssafy.cobaltcoffee.home.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}