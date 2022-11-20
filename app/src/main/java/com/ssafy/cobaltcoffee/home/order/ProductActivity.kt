package com.ssafy.cobaltcoffee.home.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.databinding.ActivityProductBinding
import com.ssafy.cobaltcoffee.dto.Product
import com.ssafy.cobaltcoffee.repository.ProductRepository
import com.ssafy.cobaltcoffee.util.CommonUtils
import com.ssafy.smartstore.util.RetrofitCallback

private const val TAG = "ProductActivity_코발트"
class ProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductBinding

    private var product: Product = Product()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        val receiveProduct: Product = intent.getSerializableExtra("product") as Product
        ProductRepository.get().getProduct(receiveProduct.id, ProductCallback())
    }

    inner class ProductCallback: RetrofitCallback<Product> {
        override fun onSuccess(code: Int, result: Product) {
            product = result as Product
            Log.d(TAG, "init: ${product}")

            binding.apply {
                Glide.with(baseContext)
                    .load("${ApplicationClass.MENU_IMGS_URL}${product.image}")
                    .transform(CenterCrop())
                    .into(productImage)
                productName.text = product.name
                productPrice.text = CommonUtils.makeComma(product.price)
            }
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "상품 정보 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }
}