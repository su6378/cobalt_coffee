package com.ssafy.cobaltcoffee.home.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
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
}