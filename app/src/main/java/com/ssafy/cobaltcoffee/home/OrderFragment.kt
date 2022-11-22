package com.ssafy.cobaltcoffee.home

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide.init
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.adapter.CurrentOrderAdapter
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.databinding.FragmentOrderBinding
import com.ssafy.cobaltcoffee.dto.*
import com.ssafy.cobaltcoffee.home.order.ProductListActivity
import com.ssafy.cobaltcoffee.repository.OrderRepository
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

private const val TAG = "OrderFragment_코발트"

class OrderFragment : Fragment() {
    private lateinit var homeActivity: HomeActivity
    private lateinit var binding: FragmentOrderBinding

    private var currentOrderList: MutableList<LatestOrder> = mutableListOf()
    private lateinit var currentOrderAdapter: CurrentOrderAdapter

    private val userViewModel: UserViewModel by activityViewModels()

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
    private fun getUserInfo() {
        if (userViewModel.userId.isEmpty()) {
            val user = ApplicationClass.sharedPreferencesUtil.getUser()
            UserRepository.get().getInfo(user.id, GetUserInfoCallback())
        } else {
            UserRepository.get().getInfo(userViewModel.userId, GetUserInfoCallback())
        }
    }

    inner class GetUserInfoCallback : RetrofitCallback<HashMap<String, Any>> {
        override fun onSuccess(code: Int, result: HashMap<String, Any>) {
            val jsonString = result
            userViewModel.currentUser =
                Gson().fromJson(jsonString["user"].toString(), object : TypeToken<User>() {}.type)
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

        //viewpager
        initViewPager()

        binding.apply {
            btnOrder.setOnClickListener {
                val intent = Intent(context, ProductListActivity::class.java)
                startActivity(intent)
            }
            btnCart.setOnClickListener {
                homeActivity.cartPage()
            }
        }
    }

    private fun initViewPager() {

        binding.apply {

            orderSlider.registerLifecycle(viewLifecycleOwner)

            val list = mutableListOf<CarouselItem>().let {
                it.apply {
                    add(CarouselItem(imageDrawable = R.drawable.banner7))
                    add(CarouselItem(imageDrawable = R.drawable.banner8))
                    add(CarouselItem(imageDrawable = R.drawable.banner9))
                }
            }

            orderSlider.setData(list)
        }
    }

    //최근 주문 내역
    private fun initAdpater() {

        getCurrentOrder()

        binding.apply {
            currentOrderAdapter = CurrentOrderAdapter(currentOrderList)
            currentOrderAdapter.setItemClickListener(object :
                CurrentOrderAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, orderId: Int) {
                    homeActivity.orderDetailPage(orderId)
                }
            })

            orderRv.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = currentOrderAdapter
                //원래의 목록위치로 돌아오게함
                adapter!!.stateRestorationPolicy =
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        }
    }

    //최근 주문내역 가져오기 : Retrofit
    private fun getCurrentOrder() {
        OrderRepository.get()
            .getRecentOrder(userViewModel.currentUser.id, CurrentOrderListCallback())
    }

    inner class CurrentOrderListCallback : RetrofitCallback<List<LatestOrder>> {
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