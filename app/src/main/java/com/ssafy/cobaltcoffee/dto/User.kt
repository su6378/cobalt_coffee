package com.ssafy.cobaltcoffee.dto

import com.google.gson.annotations.SerializedName

data class User(val id: String,  var name: String, @SerializedName("pass") var pw: String, val stamps: Int, val stampList: ArrayList<Stamp> = ArrayList()) : java.io.Serializable
{
    constructor() : this("","","",0)
    constructor(id:String, pw:String) : this(id,"",pw,0)
}

