package com.ssafy.cobaltcoffee.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartDao {

    @Query("SELECT * FROM cart WHERE userId = (:userId)")
    suspend fun getCarts(userId: String): MutableList<CartDto>

    @Insert(onConflict = REPLACE)
    suspend fun insertCart(dto: CartDto): Long

    @Update
    suspend fun updateCart(dto: CartDto): Int

    @Query("DELETE FROM cart where id = (:id)")
    suspend fun deleteCart(id: Long): Int

    @Query("DELETE FROM cart")
    suspend fun clearCart(): Int
}


