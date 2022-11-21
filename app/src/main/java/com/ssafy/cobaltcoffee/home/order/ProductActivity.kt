package com.ssafy.cobaltcoffee.home.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.databinding.ActivityProductBinding
import com.ssafy.cobaltcoffee.dialog.CartDialog
import com.ssafy.cobaltcoffee.dto.OrderDetail
import com.ssafy.cobaltcoffee.dto.Product
import com.ssafy.cobaltcoffee.repository.ProductRepository
import com.ssafy.cobaltcoffee.util.CommonUtils
import com.ssafy.cobaltcoffee.util.RetrofitCallback

private const val TAG = "ProductActivity_코발트"
class ProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductBinding

    private var product: Product = Product()
    private var orderDetail: OrderDetail = OrderDetail().apply {
        this.quantity = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        initTb()
        initBtn()
        val receiveProduct: Product = intent.getSerializableExtra("product") as Product
        ProductRepository.get().getProduct(receiveProduct.id, ProductCallback())
    }

    //툴바 적용하기
    private fun initTb() {
        binding.apply {
            setSupportActionBar(productToolBar.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            productToolBar.toolbarTitle.text = "메뉴 정보"
        }
    }

    private fun initBtn() {
        binding.apply {
            quantityMinus.setOnClickListener {
                if (orderDetail.quantity > 1) {
                    orderDetail.quantity -= 1
                    productQty.text = orderDetail.quantity.toString()
                }
            }
            quantityAdd.setOnClickListener {
                orderDetail.quantity += 1
                productQty.text = orderDetail.quantity.toString()
            }
            cartBtn.setOnClickListener {
                if (orderDetail.productId == 0) {
                    showCartDialog("잠시 후에 다시 시도해주세요.")
                    return@setOnClickListener
                }
                showCartDialog("선택하신 상품을 장바구니에 담았습니다.")
            }
            orderBtn.setOnClickListener {
                if (orderDetail.productId == 0) {
                    showCartDialog("잠시 후에 다시 시도해주세요.")
                    return@setOnClickListener
                }
            }
        }
    }

    private fun showCartDialog(content: String){
        val dialog = CartDialog(this)
        dialog.setOnOKClickedListener {

        }
        dialog.show(content)
    }

    //뒤로가기 버튼 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
//                overridePendingTransition(0, 0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class ProductCallback: RetrofitCallback<Product> {
        override fun onSuccess(code: Int, result: Product) {
            product = result

            binding.apply {
                Glide.with(baseContext)
                    .load("${ApplicationClass.MENU_IMGS_URL}${product.image}")
                    .transform(CenterCrop())
                    .into(productImage)
                productName.text = product.name
                productPrice.text = CommonUtils.makeComma(product.price)
                productContent.text = product.content
                productQty.text = orderDetail.quantity.toString()
                productKcal.text = "${product.kcal} kcal"

                orderDetail.productId = product.id

                if (!product.isNew) {
                    badgeNew.visibility = View.GONE
                }
                if (!product.isBest) {
                    badgeBest.visibility = View.GONE
                }
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