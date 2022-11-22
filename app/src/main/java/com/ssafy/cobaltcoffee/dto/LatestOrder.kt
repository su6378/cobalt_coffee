package com.ssafy.cobaltcoffee.dto

import com.google.gson.annotations.SerializedName
import java.util.*

// o_id 기준으로 분류하고, img는 그 중 하나로 사용하기
data class LatestOrder(
    var img: String,
    var orderCnt: Int,
    var userId: String,
    var orderId: Int,
    var productName: String,
    var orderDate: Date,
    var orderCompleted: Char = 'N',
    var productPrice: Int,
    var type: String,
    var totalPrice: Int = 0,
    var productId: Int = 0
) {
    constructor(): this("", 0, "", 0, "", Date(), ' ', 0, "", 0, 0)
}