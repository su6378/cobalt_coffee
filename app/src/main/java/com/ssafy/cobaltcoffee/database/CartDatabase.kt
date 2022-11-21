package com.ssafy.cobaltcoffee.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CartDto::class], version = 1)
abstract class CartDatabase: RoomDatabase(){
    abstract fun cartDao(): CartDao
}


