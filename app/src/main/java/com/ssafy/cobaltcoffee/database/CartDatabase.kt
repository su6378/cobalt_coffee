package com.ssafy.cobaltcoffee.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ssafy.cobaltcoffee.repository.CartRepository

@Database(entities = [CartDto::class], version = 1, exportSchema = false)
abstract class CartDatabase: RoomDatabase(){
    abstract fun cartDao(): CartDao

    companion object{

        @Volatile
        private var INSTANCE : CartDatabase? =null

        fun initialize(context: Context) : CartDatabase?{
            if(INSTANCE == null){
                synchronized(CartDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CartDatabase::class.java,
                        "cobalt-cafe.db"
                    ).build()
                }
            }
            return INSTANCE
        }

//        fun get() : CartRepository {
//            return INSTANCE ?:
//            throw IllegalStateException("CartRepository must be initialized")
//        }
    }
}


