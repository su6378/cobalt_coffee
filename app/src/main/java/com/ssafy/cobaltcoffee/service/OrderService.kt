package com.ssafy.cobaltcoffee.service

import com.ssafy.cobaltcoffee.dto.Order
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderService {
    // 주문정보 저장
    @POST("/rest/order")
    fun makeOrder(): Call<Order>

    // 주문 상세내역 반환
    @GET("/rest/order/{orderId}")
    fun getOrderDetail(@Path("orderId") orderId: String): Call<List<Map<String, Any>>>

    // 사용자의 최근 5개 주문 반환
    @GET("/rest/order/recent")
    fun getRecentOrder(@Query("id") id: String): Call<List<Map<String, Any>>>
}