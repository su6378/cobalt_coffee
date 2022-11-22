package com.ssafy.cobaltcoffee.service

import com.ssafy.cobaltcoffee.dto.Coupon
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CouponService {
    // 사용자의 쿠폰 목록 반환
    @GET("/rest/coupon")
    fun getCouponList(userId: String): Call<List<Coupon>>

    // 사용자에게 쿠폰 발급
    @POST("/rest/coupon/new")
    fun addCoupon(@Body coupon: Coupon): Call<Boolean>

    // 사용한 쿠폰 삭제
    @PUT("/rest/coupon/use/{couponId}")
    fun useCoupon(@Path("couponId") couponId: Int): Call<Boolean>
}