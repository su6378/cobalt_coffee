package com.ssafy.cobaltcoffee.repository

import android.content.Context
import com.ssafy.cobaltcoffee.dto.Coupon
import com.ssafy.cobaltcoffee.dto.Product
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import com.ssafy.cobaltcoffee.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "CouponRepository_코발트"
class CouponRepository(context: Context) {
    fun getCouponList(userId: String, callback: RetrofitCallback<List<Coupon>>)  {
        RetrofitUtil.couponService.getCouponList(userId).enqueue(object : Callback<List<Coupon>> {
            override fun onResponse(call: Call<List<Coupon>>, response: Response<List<Coupon>>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<List<Coupon>>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    fun addCoupon(coupon: Coupon, callback: RetrofitCallback<Boolean>)  {
        RetrofitUtil.couponService.addCoupon(coupon).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    fun useCoupon(couponId: Int, callback: RetrofitCallback<Boolean>)  {
        RetrofitUtil.couponService.useCoupon(couponId).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    companion object {
        private var INSTANCE : CouponRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CouponRepository(context)
            }
        }

        fun get(): CouponRepository {
            return INSTANCE ?:
            throw IllegalStateException("CouponRepository must be initialized")
        }
    }
}