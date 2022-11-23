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

private const val TAG = "CartAdapter_코발트"
class CartAdapter(var cartList: List<CartDto>): RecyclerView.Adapter<CartAdapter.CartHolder>(){
    inner class CartHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val productImage = itemView.findViewById<ImageView>(R.id.cart_img)

        val menuName = itemView.findViewById<TextView>(R.id.menu_name)
        val menuNameEng = itemView.findViewById<TextView>(R.id.menu_name_eng)
        val menuPrice = itemView.findViewById<TextView>(R.id.menu_price)
        val menuPriceTotal = itemView.findViewById<TextView>(R.id.menu_totalPrice)
        val menuType = itemView.findViewById<TextView>(R.id.menu_type)

        val productQty = itemView.findViewById<TextView>(R.id.product_qty)
        val quantityMinus = itemView.findViewById<ImageView>(R.id.quantity_minus)
        val quantityAdd = itemView.findViewById<ImageView>(R.id.quantity_add)

        val deleteProduct = itemView.findViewById<ImageView>(R.id.delete_product)

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

            productQty.text = item.quantity.toString()

            quantityMinus.setOnClickListener {
                minusClickListner.onClick(it,layoutPosition,cartList[layoutPosition].productId)
            }

            quantityAdd.setOnClickListener {
                plusClickListner.onClick(it,layoutPosition,cartList[layoutPosition].productId)
            }

            deleteProduct.setOnClickListener{
                closeClickListner.onClick(it, layoutPosition, cartList[layoutPosition].productId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_cart, parent, false)
        return CartHolder(view)
    }

    override fun onBindViewHolder(holder: CartHolder, position: Int) {
        holder.bindInfo(cartList[position])
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    interface PlusClickListener {
        fun onClick(view: View, position: Int, productId:Int)
    }

    interface MinusClickListener {
        fun onClick(view: View, position: Int, productId:Int)
    }

    interface CloseClickListener {
        fun onClick(view: View, position: Int, productId:Int)
    }

    private lateinit var plusClickListner: PlusClickListener
    private lateinit var minusClickListner: MinusClickListener
    private lateinit var closeClickListner: CloseClickListener

    fun setPlusClickListener(plusClickListner: PlusClickListener) {
        this.plusClickListner = plusClickListner
    }

    fun setMinusClickListener(minusClickListner: MinusClickListener) {
        this.minusClickListner = minusClickListner
    }

    fun setCloseClickListener(closeClickListner: CloseClickListener) {
        this.closeClickListner = closeClickListner
    }
}
