package com.ssafy.cobaltcoffee.service

import com.ssafy.cobaltcoffee.dto.Coupon
import com.ssafy.cobaltcoffee.dto.CouponDetail
import com.ssafy.cobaltcoffee.dto.CouponType
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CouponService {
    // 사용자의 쿠폰 목록 반환
    @GET("/rest/coupon/")
    fun getCouponList(@Query("userId") userId: String): Call<List<CouponDetail>>

    // 사용자에게 쿠폰이 있는지 확인
    @GET("/rest/coupon/check")
    fun check(@Query("userId") userId: String, @Query("couponTypeId") couponTypeId: Int): Call<Int>

    // 사용자에게 쿠폰 발급
    @POST("/rest/coupon/new")
    fun addCoupon(@Body coupon: Coupon): Call<CouponType>

    // 사용한 쿠폰 삭제
    @PUT("/rest/coupon/use/{couponId}")
    fun useCoupon(@Path("couponId") couponId: Int): Call<Boolean>
}