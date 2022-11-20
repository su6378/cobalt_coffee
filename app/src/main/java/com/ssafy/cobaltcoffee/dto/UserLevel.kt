package com.ssafy.cobaltcoffee.dto

data class UserLevel(
    var title: String,
    var unit: Int,
    var max: Int,
    var color: String
){
    companion object{
        val levelTitleArr = arrayOf("BLUE", "BRONZE", "SILVER", "GOLD", "VIP")

        var userInfoList = arrayOf(
            UserLevel(levelTitleArr[0], 10, 30, "blue"),
            UserLevel(levelTitleArr[1], 15, 30, "bronze"),
            UserLevel(levelTitleArr[2], 20, 30, "silver"),
            UserLevel(levelTitleArr[3], 25, 30, "gold"),
            UserLevel(levelTitleArr[4], Int.MAX_VALUE, 30, "cobalt")
        )
    }
}