package com.ssafy.cobaltcoffee.home.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.ActivityPayDoneBinding
import com.ssafy.cobaltcoffee.dialog.CouponDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class PayDoneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayDoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayDoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        CoroutineScope(Dispatchers.Main).async {
            test()
        }

        binding.apply {
            Glide.with(baseContext)
                .load(R.drawable.coffee_banner)
                .transform(CenterCrop())
                .into(imageView)

            closeBtn.setOnClickListener {
                finish()
            }
        }
    }

    suspend private fun test() {
        delay(1_000)
        showCouponDialog("테스트 쿠폰이 발급되었습니다.")
    }

    private fun showCouponDialog(content: String) {
        val dialog = CouponDialog(this)
        dialog.setOnOKClickedListener {

        }
        dialog.show(content)
    }
}