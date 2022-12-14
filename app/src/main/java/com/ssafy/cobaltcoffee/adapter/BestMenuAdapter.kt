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

private const val TAG = "BestMenuAdapter_코발트"
class BestMenuAdapter(var productList:List<Product>) :RecyclerView.Adapter<BestMenuAdapter.BestMenuHolder>(){

    inner class BestMenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val menuName = itemView.findViewById<TextView>(R.id.bm_name)
        val menuImage = itemView.findViewById<ImageView>(R.id.bm_img)

        fun bindInfo(product : Product){
            menuName.text = product.name
            Glide.with(itemView)
                .load("${ApplicationClass.MENU_IMGS_URL}${product.image}")
                .into(menuImage)

            itemView.setOnClickListener{
                itemClickListner.onClick(it, layoutPosition, productList[layoutPosition].id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestMenuHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_best_menu, parent, false)
        return BestMenuHolder(view)
    }

    override fun onBindViewHolder(holder: BestMenuHolder, position: Int) {
        holder.apply{
            bindInfo(productList[position])
        }
    }

    override fun getItemCount(): Int {
        return productList.size
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

