package com.ssafy.cobaltcoffee.dto

data class User(val id: String, var pw: String,var name: String, var nickname: String) : java.io.Serializable
{
    constructor() : this("","","","")
}

