package com.ssafy.cobaltcoffee.dto

import java.util.Date

class Order(
    val id: Int,
    val userId: String,
    val orderTable: String,
    val orderTime: Date,
    val completed: Char,
    val details: List<OrderDetail>,
    val stamp: Stamp
) : java.io.Serializable {
    constructor(): this(0, "", "", Date(), ' ', listOf<OrderDetail>(), Stamp())
}