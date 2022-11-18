package com.ssafy.cobaltcoffee.dto

class Product(
    val id: Int,
    val name: String,
    val type: String,
    val price: Int,
    val image: String
) {
    constructor(): this(0, "", "", 0, "")
}