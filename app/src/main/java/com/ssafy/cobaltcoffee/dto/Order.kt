package com.ssafy.cobaltcoffee.dto

import java.util.Date

class Order(
    val id: Int,
    var userId: String,
    var orderTable: String,
    val orderTime: Date,
    var completed: Char,
    var details: List<OrderDetail>,
    val stamp: Stamp
) : java.io.Serializable {
    constructor(): this(0, "", "", Date(), ' ', listOf<OrderDetail>(), Stamp())
}