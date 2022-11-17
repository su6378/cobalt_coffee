package com.ssafy.cobaltcoffee.dto

data class User(val id: String, var name: String, var pw: String, val stamps: Int, val stampList: ArrayList<Stamp> = ArrayList()) : java.io.Serializable
{
    constructor() : this("","","",0)
    constructor(id:String, pw:String) : this(id,"",pw,0)
}

