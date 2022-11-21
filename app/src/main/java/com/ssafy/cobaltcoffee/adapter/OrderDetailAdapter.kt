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
import com.ssafy.cobaltcoffee.dto.OrderDetail
import com.ssafy.cobaltcoffee.dto.Product
import com.ssafy.cobaltcoffee.dto.UserOrderDetail
import com.ssafy.cobaltcoffee.response.OrderDetailResponse
import com.ssafy.cobaltcoffee.util.CommonUtils
import java.util.Date

private const val TAG = "MenuAdapter_코발트"
class OrderDetailAdapter(var orderDetailList:List<OrderDetailResponse>) :RecyclerView.Adapter<OrderDetailAdapter.OrderDetailHolder>(){

    inner class OrderDetailHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val menuName = itemView.findViewById<TextView>(R.id.menu_name)
        val menuImage = itemView.findViewById<ImageView>(R.id.od_img)
        val menuPrice = itemView.findViewById<TextView>(R.id.menu_price)
        val totalPrice = itemView.findViewById<TextView>(R.id.menu_totalPrice)
        val totalCount = itemView.findViewById<TextView>(R.id.menu_totalCount)

        fun bindInfo(orderDetail : OrderDetailResponse){

            menuName.text = orderDetail.productName

            Glide.with(itemView)
                .load("${ApplicationClass.MENU_IMGS_URL}${orderDetail.img}")
                .into(menuImage)

            menuPrice.text = CommonUtils.makeComma(orderDetail.unitPrice)
            totalPrice.text = CommonUtils.makeComma(orderDetail.totalPrice)
            totalCount.text = "총 ${orderDetail.quantity}잔"

            itemView.setOnClickListener{
                itemClickListner.onClick(it, layoutPosition, orderDetailList[layoutPosition].productId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_order_detail, parent, false)
        return OrderDetailHolder(view)
    }

    override fun onBindViewHolder(holder: OrderDetailHolder, position: Int) {
        holder.apply{
            bindInfo(orderDetailList[position])
        }
    }

    override fun getItemCount(): Int {
        return orderDetailList.size
    }

    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View,  position: Int, productId:Int)
    }

    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener
    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}

