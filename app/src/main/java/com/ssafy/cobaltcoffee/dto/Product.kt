package com.ssafy.cobaltcoffee.dto

class Product(
    val id: Int,
    val name: String,
    val type: String,
    val price: Int,
    val image: String,
    val isNew: Boolean,
    val isBest: Boolean
) {
    constructor(): this(0, "", "", 0, "", false, false)
}