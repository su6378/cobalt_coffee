package com.ssafy.cobaltcoffee.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssafy.cobaltcoffee.dto.LatestOrder
import com.ssafy.cobaltcoffee.dto.Order
import com.ssafy.cobaltcoffee.response.OrderDetailResponse
import com.ssafy.cobaltcoffee.util.RetrofitUtil
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "OrderRepository_코발트"
class OrderRepository(context: Context) {

    fun makeOrder(order: Order, callback: RetrofitCallback<Boolean>)  {
        RetrofitUtil.orderService.makeOrder(order).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    //사용자 주문 내역 가져오기
    fun getAllOrder(userId: String, callback: RetrofitCallback<List<LatestOrder>>)  {
        RetrofitUtil.orderService.getAllOrder(userId).enqueue(object : Callback<List<LatestOrder>> {
            override fun onResponse(call: Call<List<LatestOrder>>, response: Response<List<LatestOrder>>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<List<LatestOrder>>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    //최근 주문내역 5개 가져오기
    fun getRecentOrder(userId: String, callback: RetrofitCallback<List<LatestOrder>>)  {
        RetrofitUtil.orderService.getRecentOrder(userId).enqueue(object : Callback<List<LatestOrder>> {
            override fun onResponse(call: Call<List<LatestOrder>>, response: Response<List<LatestOrder>>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<List<LatestOrder>>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    //주문 상세 내역 가져오기
    fun getOrderDetails(orderId: Int): LiveData<List<OrderDetailResponse>> {
        val responseLiveData: MutableLiveData<List<OrderDetailResponse>> = MutableLiveData()
        val orderDetailRequest: Call<List<OrderDetailResponse>> = RetrofitUtil.orderService.getOrderDetail(orderId)

        orderDetailRequest.enqueue(object : Callback<List<OrderDetailResponse>> {
            override fun onResponse(call: Call<List<OrderDetailResponse>>, response: Response<List<OrderDetailResponse>>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        responseLiveData.value = res
                    }
                    Log.d(TAG, "onResponse: $res")
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<OrderDetailResponse>>, t: Throwable) {
                Log.d(TAG, t.message ?: "주문 상세 내역 받아오는 중 통신오류")
            }
        })

        return responseLiveData
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