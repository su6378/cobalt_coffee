package com.ssafy.cobaltcoffee.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartDao {

    @Query("SELECT * FROM cart")
    fun getCarts(): LiveData<MutableList<CartDto>>

    @Query("SELECT * FROM cart WHERE userId = (:userId)")
    fun getCartsById(userId: String): LiveData<MutableList<CartDto>>

    @Insert(onConflict = REPLACE)
    suspend fun insertCart(dto: CartDto): Long

    @Update
    suspend fun updateCart(dto: CartDto): Int

    @Query("DELETE FROM cart where id = (:id)") //해당 상품 제거
    suspend fun deleteCart(id: Long): Int

    @Query("DELETE FROM cart where userId = (:userId)") //주문 완료 후 해당 오더넘버에 주문 목록 삭제
    suspend fun clearCart(userId: String): Int
}


