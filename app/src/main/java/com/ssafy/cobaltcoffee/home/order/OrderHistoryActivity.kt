package com.ssafy.cobaltcoffee.home.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.adapter.OrderHistoryAdapter
import com.ssafy.cobaltcoffee.databinding.ActivityOrderHistoryBinding
import com.ssafy.cobaltcoffee.dto.LatestOrder
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.home.HomeActivity
import com.ssafy.cobaltcoffee.repository.OrderRepository
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel

private const val TAG = "LatestOrderActivity_코발트"
class OrderHistoryActivity : AppCompatActivity() {
    private lateinit var homeActivity: HomeActivity
    private lateinit var binding: ActivityOrderHistoryBinding
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter
    private var latestOrderList: MutableList<LatestOrder> = mutableListOf()

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
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

    //뒤로가기 버튼 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
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
            orderHistoryAdapter = OrderHistoryAdapter(this@OrderHistoryActivity, latestOrderList)
            orderHistoryAdapter.setItemClickListener(object :
                OrderHistoryAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, orderId: Int) {
                    homeActivity.orderDetailPage(orderId)
                }
            })

            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@OrderHistoryActivity, LinearLayoutManager.VERTICAL, false)
                adapter = orderHistoryAdapter
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
            orderHistoryAdapter.latestOrderList = latestOrderList
            orderHistoryAdapter.notifyDataSetChanged()
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "최근 주문 내역 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }
}