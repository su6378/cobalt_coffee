package com.ssafy.cobaltcoffee.coupon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.adapter.CouponAdapter
import com.ssafy.cobaltcoffee.databinding.ActivityCouponBinding
import com.ssafy.cobaltcoffee.dto.CouponDetail
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.repository.CouponRepository
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel

private const val TAG = "CouponActivity_코발트"
class CouponActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCouponBinding
    private val userViewModel : UserViewModel by viewModels()

    private lateinit var couponAdapter: CouponAdapter
    private var couponList: MutableList<CouponDetail> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCouponBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        userViewModel.currentUser = intent.getSerializableExtra("user") as User

        initTb()

        Log.d(TAG, "init: ${userViewModel.currentUser.id}")
        CouponRepository.get().getCouponList(userViewModel.currentUser.id, CouponListCallback())

        binding.apply{
            couponAdapter = CouponAdapter(this@CouponActivity, couponList)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(DividerItemDecoration(context, 1))
                adapter = couponAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            closeBtn.setOnClickListener {
                finish()
            }
        }
    }

    private fun initTb() {
        binding.apply {
            setSupportActionBar(couponToolBar.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            couponToolBar.toolbarTitle.text = "쿠폰"
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

    inner class CouponListCallback: RetrofitCallback<List<CouponDetail>> {
        override fun onSuccess(code: Int, result: List<CouponDetail>) {
            couponList = result as MutableList<CouponDetail>
            couponAdapter.couponList = couponList
            couponAdapter.notifyDataSetChanged()
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "쿠폰 정보 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }
}