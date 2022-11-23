package com.ssafy.cobaltcoffee.dto

data class CouponType(
    val id: Int,
    val name: String,
    val discountRate: Int
) {
    constructor() : this(0, "", 0)
}