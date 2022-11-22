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

private const val TAG = "StampCardAdapter_코발트"
class StampCardAdapter(var stampList:List<Int>) :RecyclerView.Adapter<StampCardAdapter.StampCardHolder>(){

    inner class StampCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val stampImage = itemView.findViewById<ImageView>(R.id.stampCard_img)

        fun bindInfo(stampCount : Int){
            if (stampCount == 1){ //적립한 스탬프 이미지 넣기
                Glide.with(itemView).load(R.drawable.ic_logo).override(200,200).centerCrop().into(stampImage)
            }else{ //default 이미지
                Glide.with(itemView).load(R.drawable.ic_logo_black).override(200,200).centerCrop().into(stampImage)
            }

            itemView.setOnClickListener{
                itemClickListner.onClick(it, layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StampCardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_stamp_card, parent, false)
        return StampCardHolder(view)
    }

    override fun onBindViewHolder(holder: StampCardHolder, position: Int) {
        holder.apply{
            bindInfo(stampList[position])
        }
    }

    override fun getItemCount(): Int {
        return stampList.size
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

