package com.ssafy.cobaltcoffee.dto

import com.google.gson.annotations.SerializedName

data class User(
    val id: String,
    var name: String,
    @SerializedName("pass") var pw: String,
    var stamps: Int,
    @SerializedName("push") var isPush: Boolean = true,
    @SerializedName("location") var isLocation: Boolean = true,
    @SerializedName("marketing") var isMarketing: Boolean = true,
    @SerializedName("leave") var isLeave : Boolean = false,
    val stampList: ArrayList<Stamp> = ArrayList()
) : java.io.Serializable
{
    constructor() : this("","","",0,false,false,false)
    constructor(id:String, pw:String) : this(id,"",pw,0)
}

