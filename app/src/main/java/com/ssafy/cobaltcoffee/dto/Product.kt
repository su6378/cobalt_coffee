package com.ssafy.cobaltcoffee.dto

import com.google.gson.annotations.SerializedName

class Product(
    val id: Int,
    val name: String,
    val type: String,
    val price: Int,
    @SerializedName("img") val image: String,
    @SerializedName("new") val isNew: Boolean,
    @SerializedName("best") val isBest: Boolean
) {
    constructor(): this(0, "", "", 0, "", false, false)
}