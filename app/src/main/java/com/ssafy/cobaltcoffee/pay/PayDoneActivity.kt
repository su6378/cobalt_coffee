package com.ssafy.cobaltcoffee.pay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.ActivityPayDoneBinding
import com.ssafy.cobaltcoffee.dialog.CouponDialog
import com.ssafy.cobaltcoffee.dto.Coupon
import com.ssafy.cobaltcoffee.dto.CouponType
import com.ssafy.cobaltcoffee.home.order.ProductActivity
import com.ssafy.cobaltcoffee.repository.CouponRepository
import com.ssafy.cobaltcoffee.repository.ProductRepository
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.cobaltcoffee.util.CommonUtils
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import kotlinx.coroutines.*

private const val TAG = "PayDoneActivity_코발트"
class PayDoneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayDoneBinding
    private var userId: String = "test123@naver.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayDoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        couponCheck()
        
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

    private fun couponCheck() {
        CoroutineScope(Dispatchers.IO).launch {
            UserRepository.get().getStamp(userId, UserStampCallback())
        }
    }

    private fun showCouponDialog(content: String) {
        val dialog = CouponDialog(this)
        dialog.setOnOKClickedListener {

        }
        dialog.show(content)
    }

    inner class UserStampCallback : RetrofitCallback<Int> {
        override fun onSuccess(code: Int, result: Int) {
            val stampCnt = result
            for (i in 1 .. (stampCnt / 10) + 1) {
                CouponRepository.get().check(userId, i, CouponCheckCallback())
            }
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "쿠폰 정보 확인하는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }

    inner class CouponCheckCallback : RetrofitCallback<Boolean> {
        override fun onSuccess(code: Int, result: Boolean) {
            val isCoupon: Boolean = result
            if (!isCoupon) {
                CouponRepository.get().addCoupon(Coupon(), CouponAddCallback())

            }
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "쿠폰 정보 확인하는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }

    inner class CouponAddCallback : RetrofitCallback<CouponType> {
        override fun onSuccess(code: Int, result: CouponType) {
            val couponResult: CouponType = result

            CouponDialog(this@PayDoneActivity).apply {
                setOnOKClickedListener {  }
            }.show("[ ${couponResult.name} : ${couponResult.discountRate}% ]")
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "쿠폰 정보 확인하는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }
}