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
import com.ssafy.cobaltcoffee.database.CartDto
import com.ssafy.cobaltcoffee.dto.LatestOrder
import com.ssafy.cobaltcoffee.dto.Product
import com.ssafy.cobaltcoffee.util.CommonUtils
import com.ssafy.cobaltcoffee.util.RetrofitCallback

private const val TAG = "PayAdapter_코발트"
class PayAdapter(var cartList: List<CartDto>): RecyclerView.Adapter<PayAdapter.PayHolder>(){
    inner class PayHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val productImage = itemView.findViewById<ImageView>(R.id.pay_img)

        val menuName = itemView.findViewById<TextView>(R.id.menu_name)
        val menuNameEng = itemView.findViewById<TextView>(R.id.menu_name_eng)
        val menuPrice = itemView.findViewById<TextView>(R.id.menu_price)
        val menuPriceTotal = itemView.findViewById<TextView>(R.id.menu_totalPrice)
        val menuType = itemView.findViewById<TextView>(R.id.menu_type)


        fun bindInfo(item: CartDto){
            Glide.with(itemView)
                .load("${ApplicationClass.MENU_IMGS_URL}${item.img}")
                .into(productImage)

            menuName.text = item.productName
            menuNameEng.text = item.productName
            menuPrice.text = CommonUtils.makeComma(item.price)
            menuPriceTotal.text = CommonUtils.makeComma(item.totalPrice)
            when(item.type){
                "cookie" -> menuType.text = "쿠키"
                "tea" -> menuType.text = "차"
                "coffee" -> menuType.text = "커피"
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_pay, parent, false)
        return PayHolder(view)
    }

    override fun onBindViewHolder(holder: PayHolder, position: Int) {
        holder.bindInfo(cartList[position])
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

}
