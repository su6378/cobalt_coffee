package com.ssafy.cobaltcoffee.repository

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import com.ssafy.cobaltcoffee.dto.database.CartDatabase
import com.ssafy.cobaltcoffee.dto.database.CartsDto

private const val TAG = "CartRepository_코발트"
private const val DATABASE_NAME = "cart-database.db"
class CartRepository private constructor(context: Context){

    private val database : CartDatabase = Room.databaseBuilder(
        context.applicationContext,
        CartDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val cartDao = database.cartDao()

    suspend fun getCarts(userId : String) : MutableList<CartsDto> {
        return cartDao.getCarts(userId)
    }
    suspend fun insertCart(dto: CartsDto) = database.withTransaction{
        cartDao.insertCart(dto)
    }
    suspend  fun updateCart(dto: CartsDto) = database.withTransaction{
        cartDao.updateCart(dto)
    }
    suspend fun deleteCart(dto : CartsDto) = database.withTransaction {
        cartDao.deleteCart(dto)
    }

    companion object{
        private var INSTANCE : CartRepository? =null

        fun initialize(context: Context){
            if(INSTANCE == null){
                INSTANCE = CartRepository(context)
            }
        }

        fun get() : CartRepository {
            return INSTANCE ?:
            throw IllegalStateException("CartRepository must be initialized")
        }
    }

}


