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
import com.ssafy.cobaltcoffee.dto.Product
import com.ssafy.cobaltcoffee.dto.UserOrderDetail
import com.ssafy.cobaltcoffee.util.CommonUtils
import java.util.Date

private const val TAG = "MenuAdapter_코발트"
class CurrentOrderAdapter(var currentOrderList:List<UserOrderDetail>) :RecyclerView.Adapter<CurrentOrderAdapter.CurrentOrderHolder>(){

    inner class CurrentOrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val menuList = itemView.findViewById<TextView>(R.id.bm_name)
        val menuImage = itemView.findViewById<ImageView>(R.id.bm_img)
        val totalPrice = itemView.findViewById<TextView>(R.id.co_totalPrice)
        val orderDate = itemView.findViewById<TextView>(R.id.co_orderDate)

        fun bindInfo(currentOrder : UserOrderDetail){
            menuList.text = "${currentOrder.productName} 외 ${currentOrder.sumQuantity}잔"
            Glide.with(itemView)
                .load("${ApplicationClass.MENU_IMGS_URL}${currentOrder.img}")
                .into(menuImage)
            totalPrice.text = CommonUtils.makeComma(currentOrder.sumPrice)
            orderDate.text = CommonUtils.getFormattedString(currentOrder.orderDate)

            itemView.setOnClickListener{
                itemClickListner.onClick(it, layoutPosition, currentOrderList[layoutPosition].orderId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentOrderHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_best_menu, parent, false)
        return CurrentOrderHolder(view)
    }

    override fun onBindViewHolder(holder: CurrentOrderHolder, position: Int) {
        holder.apply{
            bindInfo(currentOrderList[position])
        }
    }

    override fun getItemCount(): Int {
        return currentOrderList.size
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

