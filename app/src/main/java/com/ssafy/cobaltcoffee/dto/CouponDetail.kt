package com.ssafy.cobaltcoffee.dto

data class CouponDetail(
    val couponId: Int,
    val couponTypeId: Int,
    val couponName: String,
    val discountRate: Int,
    val userId: String,
    val isUse: Boolean
) {
    constructor() : this(0, 0, "", 0, "", false)
}