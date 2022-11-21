package com.ssafy.cobaltcoffee.home.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.adapter.CurrentOrderAdapter
import com.ssafy.cobaltcoffee.adapter.OrderDetailAdapter
import com.ssafy.cobaltcoffee.databinding.ActivityOrderDetailBinding
import com.ssafy.cobaltcoffee.dto.LatestOrder
import com.ssafy.cobaltcoffee.repository.OrderRepository
import com.ssafy.cobaltcoffee.response.OrderDetailResponse
import com.ssafy.cobaltcoffee.service.OrderService
import com.ssafy.cobaltcoffee.util.CommonUtils
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "OrderDetailActivity_코발트"
class OrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailBinding
    private var orderId = -1

    private lateinit var orderDetailAdapter: OrderDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderId = intent.getIntExtra("orderId",-1)
        initTb()
        init()
    }

    //툴바 적용하기
    private fun initTb() {
        binding.apply {
            setSupportActionBar(orderDetailToolBar.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            orderDetailToolBar.toolbarTitle.text = "주문상세"
        }
    }

    //뒤로가기 버튼 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(0, 0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init(){
        val orderDetails = OrderRepository.get().getOrderDetails(orderId)
        orderDetails.observe(
            this,
            { orderDetails ->
                orderDetails.let {
                    orderDetailAdapter = OrderDetailAdapter(orderDetails)
                }

                binding.odRv.apply {
                    val linearLayoutManager = LinearLayoutManager(context)
                    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                    layoutManager = linearLayoutManager
                    adapter = orderDetailAdapter
                    //원래의 목록위치로 돌아오게함
                    adapter!!.stateRestorationPolicy =
                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }

                setOrderDetailScreen(orderDetails)

            }
        )
    }

    // OrderDetail 페이지 화면 구성
    private fun setOrderDetailScreen(orderDetails: List<OrderDetailResponse>){
        binding.odOrderDate.text = CommonUtils.getFormattedString(orderDetails[0].orderDate)
        var totalPrice = 0
        orderDetails.forEach { totalPrice += it.totalPrice }
//        binding.tvTotalPrice.text = "$totalPrice 원"
    }
}