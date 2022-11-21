package com.ssafy.cobaltcoffee.dto

import com.google.gson.annotations.SerializedName

class Product(
    var id: Int,
    val name: String,
    val type: String,
    val price: Int,
    @SerializedName("img") val image: String,
    @SerializedName("new") val isNew: Boolean,
    @SerializedName("best") val isBest: Boolean
) : java.io.Serializable {
    constructor(): this(0, "", "", 0, "", false, false)
}