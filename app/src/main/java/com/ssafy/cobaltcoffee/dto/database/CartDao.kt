package com.ssafy.cobaltcoffee.dto.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartDao {

    @Query("select * from cart where userId = (:userId)")
    suspend fun getCarts(userId: String) : MutableList<CartsDto>

    @Insert(onConflict = REPLACE)
    suspend fun insertCart(dto : CartsDto)

    @Update
    suspend fun updateCart(dto : CartsDto)

    @Delete
    suspend fun deleteCart(dto : CartsDto)

//   //id로 삭제할려면 Query로 작성해야함
//    @Query("delete from cart where id = (:id)")
//    suspend fun deleteNodeById(id: Long)
}


