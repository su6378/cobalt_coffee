package com.ssafy.cobaltcoffee.adapter

import android.content.Context
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
import com.ssafy.cobaltcoffee.util.CommonUtils

private const val TAG = "LatestOrderAdapter_코발트"
class LatestOrderAdapter(val context: Context, var latestOrderList: List<LatestOrder>): RecyclerView.Adapter<LatestOrderAdapter.LatestOrderHolder>(){
    inner class LatestOrderHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val menuList = itemView.findViewById<TextView>(R.id.co_list)
        val menuImage = itemView.findViewById<ImageView>(R.id.co_img)
        val totalPrice = itemView.findViewById<TextView>(R.id.co_totalPrice)
        val orderDate = itemView.findViewById<TextView>(R.id.co_orderDate)

        fun bindInfo(currentOrder : LatestOrder){
            if(currentOrder.orderCnt > 1){
                menuList.text = "${currentOrder.productName} 외 ${currentOrder.orderCnt -1}건"  //외 x건
            }else{
                menuList.text = currentOrder.productName
            }

            Glide.with(itemView)
                .load("${ApplicationClass.MENU_IMGS_URL}${currentOrder.img}")
                .into(menuImage)

            totalPrice.text = CommonUtils.makeComma(currentOrder.totalPrice)
            orderDate.text = CommonUtils.getFormattedString(currentOrder.orderDate)

            itemView.setOnClickListener{
                itemClickListner.onClick(it, layoutPosition, latestOrderList[layoutPosition].orderId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestOrderHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_latest_order, parent, false)
        return LatestOrderHolder(view)
    }

    override fun onBindViewHolder(holder: LatestOrderHolder, position: Int) {
        holder.bindInfo(latestOrderList[position])
    }

    override fun getItemCount(): Int {
        return latestOrderList.size
    }

    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View,  position: Int, orderId:Int)
    }

    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener
    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}