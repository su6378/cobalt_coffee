package com.ssafy.cobaltcoffee.pay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.ActivityPayBinding

class PayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}