package com.ssafy.cobaltcoffee.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.dto.CouponDetail

private const val TAG = "CouponAdapter_코발트"
class CouponAdapter(var couponList: List<CouponDetail>): RecyclerView.Adapter<CouponAdapter.CouponHolder>(){
    inner class CouponHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


        fun bindInfo(item: CouponDetail) {


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponAdapter.CouponHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_coupon, parent, false)
        return CouponHolder(view)
    }

    override fun onBindViewHolder(holder: CouponHolder, position: Int) {
        holder.bindInfo(couponList[position])
    }

    override fun getItemCount(): Int {
        return couponList.size
    }

}