package com.ssafy.cobaltcoffee.home.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.dto.Product

class ProductAdapter (val context: Context, val products: ArrayList<Product>): BaseAdapter() {
    override fun getCount(): Int = products.size

    override fun getItem(position: Int): Product = products[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View = LayoutInflater.from(parent?.context).inflate(R.layout.product_lv_item, parent, false)

        val item: Product = products[position]

        val tvProductName: TextView = view.findViewById<TextView>(R.id.product_name)
        val tvProductPrice: TextView = view.findViewById<TextView>(R.id.product_price)

        tvProductName.text = item.name
        tvProductPrice.text = item.price.toString() + "원"

        return view
    }
}