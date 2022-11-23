package com.ssafy.cobaltcoffee.dto

import com.google.gson.annotations.SerializedName

data class Coupon(
    val id: Int,
    val typeId: Int,
    val userId: String,
    @SerializedName("use") val isUse: Boolean
) {
    constructor() : this(0, 0, "", false)
}