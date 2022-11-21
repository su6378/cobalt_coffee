package com.ssafy.cobaltcoffee.service

import com.ssafy.cobaltcoffee.dto.LatestOrder
import com.ssafy.cobaltcoffee.dto.Order
import com.ssafy.cobaltcoffee.dto.UserOrderDetail
import com.ssafy.cobaltcoffee.response.OrderDetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderService {
    // 주문정보 저장
    @POST("/rest/order")
    fun makeOrder(order: Order): Call<Boolean>

    // 주문 상세내역 반환
    @GET("/rest/order/{orderId}")
    fun getOrderDetail(@Path("orderId") orderId: Int): Call<List<OrderDetailResponse>>

    // 사용자의 최근 5개 주문 반환
    @GET("/rest/order/recent")
    fun getRecentOrder(@Query("id") id: String): Call<List<LatestOrder>>
}