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
import com.ssafy.cobaltcoffee.dto.LatestOrder
import com.ssafy.cobaltcoffee.dto.Product
import com.ssafy.cobaltcoffee.util.CommonUtils
import com.ssafy.cobaltcoffee.util.RetrofitCallback

private const val TAG = "CartAdapter_코발트"
class CartAdapter(var cartList: List<LatestOrder>): RecyclerView.Adapter<CartAdapter.CartHolder>(){
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

        fun bindInfo(item: LatestOrder){
            Glide.with(itemView)
                .load("${ApplicationClass.MENU_IMGS_URL}${item.img}")
                .into(productImage)

            menuName.text = item.productName
            menuNameEng.text = item.productName
            menuPrice.text = CommonUtils.makeComma(item.productPrice)
            menuPriceTotal.text = CommonUtils.makeComma(item.totalPrice)
            menuType.text = item.type
            productQty.text = item.orderCnt.toString()

            quantityMinus.setOnClickListener {
                if (item.orderCnt > 1) {
//                    cartProduct.productQty -= 1
//                    productQty.text = cartProduct.orderCnt.toString()
                }
            }
            quantityAdd.setOnClickListener {
//                cartProduct.productQty += 1
//                productQty.text = cartProduct.orderCnt.toString()
            }

//            itemView.setOnClickListener{
//                itemClickListner.onClick(it, layoutPosition, cartList[layoutPosition].orderId)
//            }
            deleteProduct.setOnClickListener{
                closeClickListner.onClick(it, layoutPosition, cartList[layoutPosition].orderId)
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

    interface ItemClickListener {
        fun onClick(view: View, position: Int, productId:Int)
    }
    interface CloseClickListener {
        fun onClick(view: View, position: Int, productId:Int)
    }

    private lateinit var itemClickListner: ItemClickListener
    private lateinit var closeClickListner: CloseClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
    fun setCloseClickListener(closeClickListner: CloseClickListener) {
        this.closeClickListner = closeClickListner
    }
}
