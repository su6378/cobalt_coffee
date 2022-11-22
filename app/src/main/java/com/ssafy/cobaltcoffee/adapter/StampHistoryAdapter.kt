package com.ssafy.cobaltcoffee.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.dto.LatestOrder
import com.ssafy.cobaltcoffee.dto.Product
import com.ssafy.cobaltcoffee.response.OrderDetailResponse
import com.ssafy.cobaltcoffee.util.CommonUtils

private const val TAG = "StampHistoryAdapter_코발트"
class StampHistoryAdapter(var orderList:List<LatestOrder>) :RecyclerView.Adapter<StampHistoryAdapter.StampHistoryHolder>(){

    inner class StampHistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val stampCount = itemView.findViewById<TextView>(R.id.stampHistory_stampCount)
        val stampDate = itemView.findViewById<TextView>(R.id.stampHistory_date)
        
        fun bindInfo(order : LatestOrder){
            Log.d(TAG, "orderList: $orderList")
            stampCount.text = "+${order.productName}"
            stampDate.text = CommonUtils.getFormattedString(order.orderDate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StampHistoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_stamp_history, parent, false)
        return StampHistoryHolder(view)
    }

    override fun onBindViewHolder(holder: StampHistoryHolder, position: Int) {
        holder.apply{
            bindInfo(orderList[position])
        }
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${orderList.size}")
        return orderList.size
    }

    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View,  position: Int)
    }

    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener
    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}

