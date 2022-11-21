package com.ssafy.cobaltcoffee.repository

import android.content.Context
import com.ssafy.cobaltcoffee.dto.Order
import com.ssafy.cobaltcoffee.util.RetrofitUtil
import com.ssafy.smartstore.util.RetrofitCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "OrderRepository_코발트"
class OrderRepository(context: Context) {
    fun makeOrder(callback: RetrofitCallback<Order>)  {
        RetrofitUtil.orderService.makeOrder().enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    fun getOrderDetail(orderId: Int, callback: RetrofitCallback<List<Map<String, Any>>>)  {
        RetrofitUtil.orderService.getOrderDetail(orderId.toString()).enqueue(object : Callback<List<Map<String, Any>>> {
            override fun onResponse(call: Call<List<Map<String, Any>>>, response: Response<List<Map<String, Any>>>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    fun getRecentOrder(orderId: Int, callback: RetrofitCallback<List<Map<String, Any>>>)  {
        RetrofitUtil.orderService.getRecentOrder(orderId.toString()).enqueue(object : Callback<List<Map<String, Any>>> {
            override fun onResponse(call: Call<List<Map<String, Any>>>, response: Response<List<Map<String, Any>>>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    companion object {
        private var INSTANCE : OrderRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = OrderRepository(context)
            }
        }

        fun get(): OrderRepository {
            return INSTANCE ?:
            throw IllegalStateException("OrderRepository must be initialized")
        }
    }
}