package com.ssafy.cobaltcoffee.dto.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(CartsDto::class), version = 1)
abstract class CartDatabase :RoomDatabase(){

    abstract fun cartDao() : CartDao

}


