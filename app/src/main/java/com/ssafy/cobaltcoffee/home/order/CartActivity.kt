package com.ssafy.cobaltcoffee.home.order

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.adapter.CartAdapter
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.database.CartDto
import com.ssafy.cobaltcoffee.databinding.ActivityCartBinding
import com.ssafy.cobaltcoffee.dialog.CartDialog
import com.ssafy.cobaltcoffee.dialog.LocationDialog
import com.ssafy.cobaltcoffee.dto.LatestOrder
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.repository.CartRepository
import com.ssafy.cobaltcoffee.repository.ProductRepository
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.cobaltcoffee.util.CommonUtils
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import com.ssafy.cobaltcoffee.viewmodel.CartViewModel
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel
import kotlinx.coroutines.*

private const val TAG = "CartActivity_코발트"

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding

    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartList : MutableList<CartDto>
    private var checkInsert = false

    private val userViewModel: UserViewModel by viewModels()
    private val REQUEST_PERMISSION_LOCATION = 10

    private lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userViewModel.currentUser = intent.getSerializableExtra("user") as User
        initTb()
        init()
    }

    //툴바 적용하기
    private fun initTb() {
        binding.apply {
            setSupportActionBar(collapsingToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        }
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

    //총 개수 총 금액 데이터 갱신
    private fun initBottom(count : Int, price : Int){
        binding.cartTotalCount.text = "${count}개"
        binding.cartTotalPrice.text = CommonUtils.makeComma(price)
    }

    private fun init() {

        // 뷰모델 연결
        cartViewModel = ViewModelProvider(this, CartViewModel.Factory(application)).get(CartViewModel::class.java)

        initAdapter()

        binding.apply {

            //주문하기 버튼 클릭 시
            orderBtn.setOnClickListener {
                if (ContextCompat.checkSelfPermission(this@CartActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@CartActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ) {
                    cartViewModel.clearCart(userViewModel.currentUser.id)
                    cartList.clear()
                    cartAdapter.cartList = cartList
                    cartAdapter.notifyDataSetChanged()
                    initBottom(0,0)

                    binding.apply {
                        cartRv.apply {
                            layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
                            adapter = cartAdapter
                            //원래의 목록위치로 돌아오게함
                            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                        }
                    }
                } else { //위치 서비스 동의하지 않은 경우 dialog 띄우기
                    showLocationDialog()
                }
            }
        }
    }

    private fun initAdapter(){
        //장바구니 상품가져오기
        cartList = mutableListOf()
        var totalCount = 0
        var totalPrice = 0
        cartViewModel.readAllData.observe(this@CartActivity) {
            if (!checkInsert){
                for(cart in it){
                    //room에 저장되어있는 장바구니 품목에서 해당 아이디만 해당하는 주문 목록을 리스트에 추가
                    if(cart.userId == userViewModel.currentUser.id){
                        cartList.add(cart)
                        totalCount += cart.quantity
                        totalPrice += cart.totalPrice
                    }
                }
                initBottom(totalCount,totalPrice)
                checkInsert = true
            }

            binding.apply {
                cartAdapter = CartAdapter(cartList)

                cartAdapter.setPlusClickListener(object : CartAdapter.PlusClickListener{
                    override fun onClick(view: View, position: Int, productId: Int) {
                        cartList[position].quantity++
                        cartList[position].totalPrice += cartList[position].price
                        cartAdapter.cartList = cartList
                        cartAdapter.notifyDataSetChanged()
                        totalCount++
                        totalPrice += cartList[position].price
                        initBottom(totalCount,totalPrice)
                        cartViewModel.update(cartList[position])
                    }
                })

                cartAdapter.setMinusClickListener(object : CartAdapter.MinusClickListener{
                    override fun onClick(view: View, position: Int, productId: Int) {

                        if (cartList[position].quantity > 1 && cartList[position].totalPrice > 0){ //가격이랑 카운트가 0보다 클 때만 버튼 작동동
                            cartList[position].quantity--
                            cartList[position].totalPrice -= cartList[position].price
                            cartAdapter.cartList = cartList
                            cartAdapter.notifyDataSetChanged()
                            totalCount--
                            totalPrice -= cartList[position].price
                            initBottom(totalCount,totalPrice)
                            cartViewModel.update(cartList[position])
                        }
                    }
                })

                cartAdapter.setCloseClickListener(object: CartAdapter.CloseClickListener {
                    override fun onClick(view: View, position: Int, productId: Int) {
                        cartViewModel.deleteCart(cartList[position])
                        totalCount -= cartList[position].quantity
                        totalPrice -= cartList[position].totalPrice
                        cartList.removeAt(position)
                        cartAdapter.cartList = cartList
                        cartAdapter.notifyDataSetChanged()
                        initBottom(totalCount,totalPrice)
                    }
                })

                cartRv.apply {
                    layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
                    adapter = cartAdapter
                    //원래의 목록위치로 돌아오게함
                    adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
            }
        }
    }

    private fun showCartDialog(content: String) {
        val dialog = CartDialog(this)
        dialog.setOnOKClickedListener {

        }
        dialog.show(content)
    }

    //위치 정보 서비스 동의 다이얼로그 생성
    private fun showLocationDialog() {
        val dialog = LocationDialog(this)
        dialog.setOnOKClickedListener {
            ActivityCompat.requestPermissions(
                this@CartActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSION_LOCATION
            )
        }
        dialog.show()
    }
}
