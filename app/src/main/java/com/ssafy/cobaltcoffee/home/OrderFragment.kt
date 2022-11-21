package com.ssafy.cobaltcoffee.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide.init
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.adapter.CurrentOrderAdapter
import com.ssafy.cobaltcoffee.adapter.ImageSliderAdapter
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.databinding.FragmentOrderBinding
import com.ssafy.cobaltcoffee.dto.*
import com.ssafy.cobaltcoffee.home.order.ProductListActivity
import com.ssafy.cobaltcoffee.repository.OrderRepository
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel
import com.ssafy.cobaltcoffee.util.RetrofitCallback

private const val TAG = "OrderFragment_코발트"
class OrderFragment : Fragment() {
    private lateinit var homeActivity: HomeActivity
    private lateinit var binding: FragmentOrderBinding

    private var currentOrderList: MutableList<LatestOrder> = mutableListOf()
    private lateinit var currentOrderAdapter: CurrentOrderAdapter

    private val userViewModel : UserViewModel by activityViewModels()

    private val images = intArrayOf(
        R.drawable.banner7,
        R.drawable.banner8,
        R.drawable.banner9
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }

    //sp에 저장 되어있는 로그인 유저의 id로 retrofit userinfo 실행
    private fun getUserInfo(){
        if (userViewModel.userId.isEmpty()){
            val user = ApplicationClass.sharedPreferencesUtil.getUser()
            UserRepository.get().getInfo(user.id,GetUserInfoCallback())
        }else{
            UserRepository.get().getInfo(userViewModel.userId,GetUserInfoCallback())
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
            btnOrder.setOnClickListener {
                val intent: Intent = Intent(context, ProductListActivity::class.java)
                startActivity(intent)
            }
            btnCart.setOnClickListener {
                Toast.makeText(context, "장바구니", Toast.LENGTH_SHORT).show()
            }

            //이미지 슬라이더
            orderSlider.offscreenPageLimit = 1
            orderSlider.adapter = ImageSliderAdapter(requireContext(), images)

            orderSlider.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicator(position)
                }
            })
            setupIndicators(images.size)
        }
    }

    //슬라이더 밑에 indicator 초기화
    private fun setupIndicators(count: Int) {
        val indicators: Array<ImageView?> = arrayOfNulls<ImageView>(count)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(16, 8, 16, 8)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bg_indicator_inactive
                )
            )
            indicators[i]?.layoutParams = params
            binding.orderIndicator.addView(indicators[i])
        }
        setCurrentIndicator(0)
    }

    //indicator 현재 위치 컬러표시
    private fun setCurrentIndicator(position: Int) {
        val childCount: Int = binding.orderIndicator.childCount
        for (i in 0 until childCount) {
            val imageView: ImageView = binding.orderIndicator.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_indicator_inactive
                    )
                )
            }
        }
    }

    //최근 주문 내역
    private fun initAdpater(){

        getCurrentOrder()

        binding.apply {
            currentOrderAdapter = CurrentOrderAdapter(currentOrderList)
            currentOrderAdapter.setItemClickListener(object : CurrentOrderAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, productId: Int) {

                }
            })

            orderRv.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = currentOrderAdapter
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
            currentOrderList = result as MutableList<LatestOrder>
            currentOrderAdapter.currentOrderList = currentOrderList
            currentOrderAdapter.notifyDataSetChanged()
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "최근 주문 내역 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }
}