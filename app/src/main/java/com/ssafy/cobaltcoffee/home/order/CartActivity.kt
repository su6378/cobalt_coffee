package com.ssafy.cobaltcoffee.home.order

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.adapter.CurrentOrderAdapter
import com.ssafy.cobaltcoffee.adapter.ImageSliderAdapter
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.databinding.ActivityCartBinding
import com.ssafy.cobaltcoffee.dialog.LocationDialog
import com.ssafy.cobaltcoffee.dialog.LogoutDialog
import com.ssafy.cobaltcoffee.dto.LatestOrder
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.repository.OrderRepository
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel

private const val TAG = "CartActivity_코발트"
class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding

    private var cartList: MutableList<LatestOrder> = mutableListOf()
    private lateinit var cartAdapter: CurrentOrderAdapter

    private val userViewModel : UserViewModel by viewModels()
    private val REQUEST_PERMISSION_LOCATION = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
//                overridePendingTransition(0, 0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //sp에 저장 되어있는 로그인 유저의 id로 retrofit userinfo 실행
    private fun getUserInfo(){
        if (userViewModel.userId.isEmpty()) {
            val user = ApplicationClass.sharedPreferencesUtil.getUser()
            UserRepository.get().getInfo(user.id, GetUserInfoCallback())
        } else {
            UserRepository.get().getInfo(userViewModel.userId, GetUserInfoCallback())
        }
    }

    inner class GetUserInfoCallback: RetrofitCallback<HashMap<String, Any>> {
        override fun onSuccess( code: Int, result: HashMap<String,Any>) {
            val jsonString = result
            userViewModel.currentUser = Gson().fromJson(jsonString["user"].toString(), object: TypeToken<User>(){}.type)
            initAdpater()
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "유저 정보 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }

    private fun init() {
        //유저 정보 갱신
        getUserInfo()

        binding.apply {

            //주문하기 버튼 클릭 시
            orderBtn.setOnClickListener {
                if(ContextCompat.checkSelfPermission(this@CartActivity,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this@CartActivity,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){


                }else{ //위치 서비스 동의하지 않은 경우 dialog 띄우기
                    showLocationDialog()
                }
            }
        }

    }

    //최근 주문 내역
    private fun initAdpater(){

        getCurrentOrder()

        binding.apply {
            cartAdapter = CurrentOrderAdapter(cartList)
            cartAdapter.setItemClickListener(object : CurrentOrderAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, productId: Int) {

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

    //최근 주문내역 가져오기 : Retrofit
    private fun getCurrentOrder(){
        OrderRepository.get().getRecentOrder(userViewModel.currentUser.id,CurrentOrderListCallback())
    }

    inner class CurrentOrderListCallback: RetrofitCallback<List<LatestOrder>> {
        override fun onSuccess(code: Int, result: List<LatestOrder>) {
            cartList = result as MutableList<LatestOrder>
            cartAdapter.currentOrderList = cartList
            cartAdapter.notifyDataSetChanged()
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "최근 주문 내역 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }

    //위치 정보 서비스 동의 다이얼로그 생성
    private fun showLocationDialog(){
        val dialog = LocationDialog(this)
        dialog.setOnOKClickedListener {
            ActivityCompat.requestPermissions(this@CartActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
        }
        dialog.show()
    }
}