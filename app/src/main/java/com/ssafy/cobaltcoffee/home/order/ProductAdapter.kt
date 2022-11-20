package com.ssafy.cobaltcoffee.home.order

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.databinding.ProductLvItemBinding
import com.ssafy.cobaltcoffee.dto.Product
import com.ssafy.cobaltcoffee.util.CommonUtils

private const val TAG = "ProductAdapter_코발트"
class ProductAdapter (val context: Context, var products: List<Product>)
    : RecyclerView.Adapter<ProductAdapter.ListHolder>() {
    inner class ListHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById<ImageView>(R.id.product_image)
        val productName: TextView = view.findViewById<TextView>(R.id.product_name)
        val productPrice: TextView = view.findViewById<TextView>(R.id.product_price)
        val tagNew: ImageView = view.findViewById<ImageView>(R.id.tag_new)
        val tagBest: ImageView = view.findViewById<ImageView>(R.id.tag_best)

        fun bindInfo(product: Product){
            Glide.with(context)
                .load("${ApplicationClass.MENU_IMGS_URL}${product.image}")
                .transform(CenterCrop())
                .into(productImage)
            productName.text = product.name
            productPrice.text = CommonUtils.makeComma(product.price)

            if (!product.isNew) tagNew.visibility = View.GONE
            if (!product.isBest) tagBest.visibility = View.GONE

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, pos)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val binding = ProductLvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holder.bindInfo(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

    interface OnItemClickListener{
        fun onItemClick(v:View, pos: Int)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }
}