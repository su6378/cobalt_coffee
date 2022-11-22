package com.ssafy.cobaltcoffee.dto

import com.google.gson.annotations.SerializedName

data class Coupon(
    val id: Int,
    val userId: String,
    val name: String,
    val discountRate: Int,
    @SerializedName("use") val isUse: Boolean
) {
    constructor() : this(0, "", "", 0, false)
}