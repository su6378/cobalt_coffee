package com.ssafy.cobaltcoffee.home.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.database.CartDto
import com.ssafy.cobaltcoffee.databinding.ActivityProductBinding
import com.ssafy.cobaltcoffee.dialog.CartDialog
import com.ssafy.cobaltcoffee.dto.OrderDetail
import com.ssafy.cobaltcoffee.dto.Product
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.repository.CartRepository
import com.ssafy.cobaltcoffee.repository.ProductRepository
import com.ssafy.cobaltcoffee.util.CommonUtils
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import com.ssafy.cobaltcoffee.viewmodel.CartViewModel
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "ProductActivity_코발트"

class ProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductBinding

    private var product: Product = Product()
    private val userViewModel: UserViewModel by viewModels()

    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartDto: CartDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        userViewModel.currentUser = intent.getSerializableExtra("user") as User

        // 뷰모델 연결
        cartViewModel = ViewModelProvider(
            this,
            CartViewModel.Factory(application)
        ).get(CartViewModel::class.java)

        cartDto = CartDto().apply { //장바구니 초기값 세팅
            this.userId = userViewModel.currentUser.id
            this.quantity = 1
        }

        initTb()
        initBtn()
        val receiveProduct: Product = intent.getSerializableExtra("product") as Product
        CoroutineScope(Dispatchers.IO).launch {
            ProductRepository.get().getProduct(receiveProduct.id, ProductCallback())
        }
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
                if (cartDto.quantity > 1) {
                    cartDto.quantity -= 1
                    productQty.text = cartDto.quantity.toString()
                }
            }
            quantityAdd.setOnClickListener {
                cartDto.quantity += 1
                productQty.text = cartDto.quantity.toString()
            }
            cartBtn.setOnClickListener {
                if (cartDto.productId == 0) {
                    showCartDialog("잠시 후에 다시 시도해주세요.")
                    return@setOnClickListener
                }

                cartDto.totalPrice = cartDto.price * cartDto.quantity
                cartViewModel.addCart(cartDto)
                showCartDialog("장바구니에 상품이 추가되었습니다.")

            }
            orderBtn.setOnClickListener {
                if (cartDto.productId == 0) {
                    showCartDialog("잠시 후에 다시 시도해주세요.")
                    return@setOnClickListener
                }
                val intent = Intent(this@ProductActivity, CartActivity::class.java)
                intent.putExtra("user", userViewModel.currentUser)
                startActivity(intent)
            }
        }
    }

    private fun showCartDialog(content: String) {
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
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class ProductCallback : RetrofitCallback<Product> {
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
                productQty.text = cartDto.quantity.toString()
                productKcal.text = "${product.kcal} kcal"

                cartDto.productId = product.id
                cartDto.productName = product.name
                cartDto.price = product.price
                cartDto.type = product.type
                cartDto.img = product.image

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