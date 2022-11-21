package com.ssafy.cobaltcoffee.dto

class OrderDetail(
    var id: Int,
    var orderId: Int,
    var productId: Int,
    var quantity: Int
) : java.io.Serializable {
    constructor(): this(0, 0, 0, 0)
}