package com.ssafy.cobaltcoffee.home.order

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
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
import kotlin.math.pow

private const val TAG = "CartActivity_코발트"

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding

    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartList : MutableList<CartDto>
    private var checkInsert = false

    private val userViewModel: UserViewModel by viewModels()

    private lateinit var cartViewModel: CartViewModel

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
    internal lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10

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
        //현재 위치
        mLocationRequest =  LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // 뷰모델 연결
        cartViewModel = ViewModelProvider(this, CartViewModel.Factory(application)).get(CartViewModel::class.java)

        initAdapter()

        binding.apply {

            //주문하기 버튼 클릭 시
            orderBtn.setOnClickListener {
                if (ContextCompat.checkSelfPermission(this@CartActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@CartActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ) {

                    startLocationUpdates()
//                    cartViewModel.clearCart(userViewModel.currentUser.id)
//                    cartList.clear()
//                    cartAdapter.cartList = cartList
//                    cartAdapter.notifyDataSetChanged()
//                    initBottom(0,0)
//
//                    binding.apply {
//                        cartRv.apply {
//                            layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
//                            adapter = cartAdapter
//                            //원래의 목록위치로 돌아오게함
//                            adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
//                        }
//                    }
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

    private fun startLocationUpdates() {
        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            locationResult.lastLocation?.let { onLocationChanged(it) }
        }
    }

    // 시스템으로 부터 받은 위치정보를 화면에 갱신해주는 메소드
    fun onLocationChanged(location: Location) {
        mLastLocation = location
        Log.d(TAG, "onLocationChanged:매장과의 거리가 ${getDistance(mLastLocation.latitude, mLastLocation.longitude)}m 입니다. ")
        Log.d(TAG, "onLocationChanged: ${mLastLocation.longitude} ${mLastLocation.latitude}")
    }

    //현재 거리 구하는 함수
    fun getDistance(lat: Double, lng: Double): Int {
        var lat_ = 36.1081964
        var lng_ = 128.413952
        val dLat = Math.toRadians(lat - lat_)
        val dLng = Math.toRadians(lng - lng_)
        val a = Math.sin(dLat / 2).pow(2.0) + Math.sin(dLng / 2).pow(2.0) * Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(lat_))
        val c = 2 * Math.asin(Math.sqrt(a))
        return (6372.8 * 1000 * c).toInt()
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
