package com.ssafy.cobaltcoffee.dto

data class Stamp (
    val id: Int,
    val userId: String,
    val orderId: Int,
    val quantity: Int,
){
    constructor():this(0,"",0,0)
}
