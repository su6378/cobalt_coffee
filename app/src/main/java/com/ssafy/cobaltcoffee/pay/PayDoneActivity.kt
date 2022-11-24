package com.ssafy.cobaltcoffee.pay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.ActivityPayDoneBinding
import com.ssafy.cobaltcoffee.dialog.CouponDialog
import com.ssafy.cobaltcoffee.dto.Coupon
import com.ssafy.cobaltcoffee.dto.CouponType
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.home.HomeActivity
import com.ssafy.cobaltcoffee.repository.CouponRepository
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.cobaltcoffee.util.CommonUtils
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel
import kotlinx.coroutines.*

private const val TAG = "PayDoneActivity_코발트"
class PayDoneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayDoneBinding
    private val userViewModel : UserViewModel by viewModels()

    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayDoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        userViewModel.currentUser = intent.getSerializableExtra("user") as User
        userId = userViewModel.currentUser.id

        val totalCount = intent.getIntExtra("totalCount", 0)
        val totalPrice = intent.getIntExtra("totalPrice", 0)

        couponCheck()
        
        binding.apply {
            Glide.with(baseContext)
                .load(R.drawable.coffee_banner)
                .transform(CenterCrop())
                .into(imageView)

            productQty.text = "${totalCount} 개"
            productPrice.text = CommonUtils.makeComma(totalPrice)

            closeBtn.setOnClickListener {
                finishPayDoneActivity()
            }
        }
    }

    override fun onBackPressed() {
        finishPayDoneActivity()
    }

    private fun finishPayDoneActivity() {
        startActivity(Intent(this@PayDoneActivity, HomeActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        finish()
    }

    private fun couponCheck() {
        CoroutineScope(Dispatchers.IO).launch {
            UserRepository.get().getStamp(userId, UserStampCallback())
        }
    }

    inner class UserStampCallback : RetrofitCallback<Int> {
        override fun onSuccess(code: Int, result: Int) {
            val stampCnt = result
            for (i in 1 .. (stampCnt / 10)) {
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

    inner class CouponCheckCallback : RetrofitCallback<Int> {
        override fun onSuccess(code: Int, result: Int) {
            if (result != 0) {
                CouponRepository.get().addCoupon(Coupon(0, result, userId, false), CouponAddCallback())
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