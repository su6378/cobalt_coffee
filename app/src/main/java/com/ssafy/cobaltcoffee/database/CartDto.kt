package com.ssafy.cobaltcoffee.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartDto(
    var userId : String = "userId",
    var productName : String = "",
    var productId: Int = 0,
    var quantity: Int = 0,
    var price : Int = 0,
    var totalPrice : Int = 0,
    var img : String = "",
    var type : String = ""
) {
    @PrimaryKey(autoGenerate = true)
//    @NonNull
    var id: Long = 0

    constructor(id:Long, userId:String,productName: String, productId:Int, quantity: Int, price: Int, totalPrice: Int,img: String,type: String): this(userId, productName,productId, quantity,price, totalPrice,img,type){
        this.id = id;
    }
}