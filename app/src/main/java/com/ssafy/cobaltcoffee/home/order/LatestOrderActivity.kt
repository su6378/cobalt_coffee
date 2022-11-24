package com.ssafy.cobaltcoffee.home.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.adapter.LatestOrderAdapter
import com.ssafy.cobaltcoffee.databinding.ActivityLatestOrderBinding
import com.ssafy.cobaltcoffee.dto.LatestOrder
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.home.HomeActivity
import com.ssafy.cobaltcoffee.repository.OrderRepository
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel

private const val TAG = "LatestOrderActivity_코발트"
class LatestOrderActivity : AppCompatActivity() {
    private lateinit var homeActivity: HomeActivity
    private lateinit var binding: ActivityLatestOrderBinding
    private lateinit var latestOrderAdapter: LatestOrderAdapter
    private var latestOrderList: MutableList<LatestOrder> = mutableListOf()

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLatestOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTb()
        init()
        initAdapter()
    }

    //sp에 저장 되어있는 로그인 유저의 id로 retrofit userinfo 실행
    private fun getUserInfo() {
        userViewModel.currentUser = intent.getSerializableExtra("user") as User
    }

    //툴바 적용하기
    private fun initTb() {
        binding.apply {
            setSupportActionBar(latestOrderToolBar.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            latestOrderToolBar.toolbarTitle.text = "주문내역"
        }
    }

    private fun init() {
        //유저 정보 갱신
        getUserInfo()

        binding.apply {
            closeBtn.setOnClickListener {
                finish()
            }
        }
    }

    private fun initAdapter() {
        getCurrentOrder()

        binding.apply {
            latestOrderAdapter = LatestOrderAdapter(this@LatestOrderActivity, latestOrderList)
            latestOrderAdapter.setItemClickListener(object :
                LatestOrderAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, orderId: Int) {
                    homeActivity.orderDetailPage(orderId)
                }
            })

            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@LatestOrderActivity, LinearLayoutManager.VERTICAL, false)
                adapter = latestOrderAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        }
    }

    //모든 주문내역 가져오기 : Retrofit
    private fun getCurrentOrder() {
        OrderRepository.get().getAllOrder(userViewModel.currentUser.id, CurrentOrderListCallback())
    }

    inner class CurrentOrderListCallback : RetrofitCallback<List<LatestOrder>> {
        override fun onSuccess(code: Int, result: List<LatestOrder>) {
            latestOrderList = result as MutableList<LatestOrder>
            latestOrderAdapter.latestOrderList = latestOrderList
            latestOrderAdapter.notifyDataSetChanged()
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "최근 주문 내역 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }
}