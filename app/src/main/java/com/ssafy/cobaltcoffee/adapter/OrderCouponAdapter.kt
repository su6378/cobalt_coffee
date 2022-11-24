package com.ssafy.cobaltcoffee.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.dto.CouponDetail
import com.ssafy.cobaltcoffee.dto.Product

private const val TAG = "OrderCouponAdapter_코발트"
class OrderCouponAdapter(var couponList:List<CouponDetail>) :RecyclerView.Adapter<OrderCouponAdapter.OrderCouponHolder>(){

    inner class OrderCouponHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val couponName = itemView.findViewById<TextView>(R.id.orderCoupon_info)

        fun bindInfo(coupon : CouponDetail){
            couponName.text = if (coupon.couponId == 0) coupon.couponName else "${coupon.couponName} [${coupon.discountRate}%]"

            itemView.setOnClickListener{
                itemClickListner.onClick(it, layoutPosition, couponList[layoutPosition].couponId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderCouponHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_order_coupon, parent, false)
        return OrderCouponHolder(view)
    }

    override fun onBindViewHolder(holder: OrderCouponAdapter.OrderCouponHolder, position: Int) {
        holder.apply{
            bindInfo(couponList[position])
        }
    }

    override fun getItemCount(): Int {
        return couponList.size
    }

    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View,  position: Int, couponId : Int)
    }

    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener
    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}

