package com.ssafy.cobaltcoffee.dto

class OrderDetail(
    val id: Int,
    val orderId: Int,
    val productId: Int,
    val quantity: Int
) : java.io.Serializable {
    constructor(): this(0, 0, 0, 0)
}