package com.ssafy.cobaltcoffee.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.withTransaction
import com.ssafy.cobaltcoffee.database.CartDao
import com.ssafy.cobaltcoffee.database.CartDatabase
import com.ssafy.cobaltcoffee.database.CartDto

private const val TAG = "CartRepository_코발트"
private const val DATABASE_NAME = "cobalt-cafe.db"
class CartRepository(private val cartDao: CartDao) {

    val realAllData : LiveData<MutableList<CartDto>> = cartDao.getCarts()

    fun getCarts(userId: String): LiveData<MutableList<CartDto>> {
        return cartDao.getCartsById(userId)
    }

    suspend fun insertCart(dto: CartDto) {
        cartDao.insertCart(dto)
    }

//    suspend fun updateCart(dto: CartDto) = database.withTransaction {
//        cartDao.updateCart(dto)
//    }
//
//    suspend fun deleteCart(dto: CartDto) = database.withTransaction {
//        cartDao.deleteCart(dto.id)
//    }
//
//    suspend fun clearCart(userId: String) = database.withTransaction {
//        cartDao.clearCart(userId)
//    }
}


