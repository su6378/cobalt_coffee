package com.ssafy.cobaltcoffee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.nikartm.button.FitButton
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.dto.CouponDetail

private const val TAG = "CouponAdapter_코발트"
class CouponAdapter(val context: Context, var couponList: List<CouponDetail>): RecyclerView.Adapter<CouponAdapter.CouponHolder>(){
    inner class CouponHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val couponUse = itemView.findViewById<FitButton>(R.id.coupon_fb)
        val couponUseTv = itemView.findViewById<TextView>(R.id.stampHistory_stampCount)
        val couponName = itemView.findViewById<TextView>(R.id.coupon_name)
        val couponDiscRate = itemView.findViewById<TextView>(R.id.coupon_disc_rate)

        fun bindInfo(item: CouponDetail) {
            when (item.isUse) {
                false -> {
                    couponUse.setBorderColor(R.color.cobalt)
                    couponUseTv.setTextColor(R.color.cobalt)
                    couponUseTv.text = "사용가능"
                }
                true -> {
                    couponUse.setBorderColor(R.color.darkGray)
                    couponUseTv.setTextColor(R.color.darkGray)
                    couponUseTv.text = "사용완료"
                }
            }

            couponName.text = item.couponName
            couponDiscRate.text = "${item.discountRate}%"
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