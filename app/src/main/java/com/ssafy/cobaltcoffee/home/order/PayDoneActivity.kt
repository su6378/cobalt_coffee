package com.ssafy.cobaltcoffee.home.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.ActivityPayDoneBinding
import com.ssafy.cobaltcoffee.dialog.CouponDialog
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
        //showCouponDialog("테스트 쿠폰이 발급되었습니다.")


        CoroutineScope(Dispatchers.IO).launch {
            UserRepository.get().getStamp("userId", UserStampCallback())



//            CouponRepository.get().check(receiveProduct.id, ProductActivity.ProductCallback())
        }
    }

    private fun showCouponDialog(content: String) {
        val dialog = CouponDialog(this)
        dialog.setOnOKClickedListener {

        }
        dialog.show(content)
    }

    inner class UserStampCallback : RetrofitCallback<Boolean> {
        override fun onSuccess(code: Int, result: Boolean) {




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
            
            
            
            
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "쿠폰 정보 확인하는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }
}