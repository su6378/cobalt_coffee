package com.ssafy.cobaltcoffee.repository

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import com.ssafy.cobaltcoffee.database.CartDatabase
import com.ssafy.cobaltcoffee.database.CartDto

private const val TAG = "CartRepository_코발트"
private const val DATABASE_NAME = "cobalt-cafe.db"
class CartRepository(context: Context) {
    private val database : CartDatabase = Room.databaseBuilder(
        context.applicationContext,
        CartDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val cartDao = database.cartDao()

    suspend fun getCarts(userId: String): MutableList<CartDto> {
        return cartDao.getCarts(userId)
    }

    suspend fun insertCart(dto: CartDto): Int = database.withTransaction {
        return@withTransaction cartDao.insertCart(dto)
    }

    suspend fun updateCart(dto: CartDto): Int = database.withTransaction {
        return@withTransaction cartDao.updateCart(dto)
    }

    suspend fun deleteCart(dto: CartDto): Int = database.withTransaction {
        return@withTransaction cartDao.deleteCart(dto.id)
    }

    suspend fun clearCart(): Int = database.withTransaction {
        return@withTransaction cartDao.clearCart()
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


