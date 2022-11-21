package com.ssafy.cobaltcoffee.dto

import com.google.gson.annotations.SerializedName
import java.util.*

// o_id 기준으로 분류하고, img는 그 중 하나로 사용하기
data class LatestOrder(
    val img: String,
    var orderCnt: Int,
    val userId: String,
    val orderId: Int,
    val productName: String,
    val orderDate: Date,
    var orderCompleted: Char = 'N',
    val productPrice: Int,
    val type: String,
    var totalPrice: Int = 0
)