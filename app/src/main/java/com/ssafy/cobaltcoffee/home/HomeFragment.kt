package com.ssafy.cobaltcoffee.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.adapter.BestMenuAdapter
import com.ssafy.cobaltcoffee.databinding.FragmentHomeBinding
import com.ssafy.cobaltcoffee.dto.Product
import com.ssafy.cobaltcoffee.home.order.ProductActivity
import com.ssafy.cobaltcoffee.home.order.ProductAdapter
import com.ssafy.cobaltcoffee.repository.ProductRepository
import com.ssafy.smartstore.util.RetrofitCallback

private const val TAG = "HomeFragment_코발트"
class HomeFragment : Fragment() {
    private lateinit var homeActivity: HomeActivity
    private lateinit var binding: FragmentHomeBinding

    private var bestMenuList: MutableList<Product> = mutableListOf()
    private lateinit var bestMenuAdapter: BestMenuAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        binding.homeBanner.setOnClickListener {

        }


    }

    private fun init() {

        getBestMenu()

        //이미지 적용 (Glide 라이브러리 사용)
        val options = RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)

        Glide.with(this).load(R.drawable.background_home).apply(options).into(binding.homeImg) //툴바 쪽 이미지
        Glide.with(this).load(R.drawable.banner).apply(options).into(binding.homeBanner) //신상품 이미지
        Glide.with(this).load(R.drawable.banner2).apply(options).into(binding.homeBanner2) //신상메뉴 이미지
        Glide.with(this).load(R.drawable.banner3).apply(options).into(binding.homeBanner3) //콜드브루 이미지

        binding.apply {
            bestMenuAdapter = BestMenuAdapter(bestMenuList)
            bestMenuAdapter.setItemClickListener(object : BestMenuAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int, productId: Int) {

                }
            })

            homeRv.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = bestMenuAdapter
                //원래의 목록위치로 돌아오게함
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        }

    }

    //BestMenu 가져오기
    private fun getBestMenu(){
        ProductRepository.get().getBestProductList(MenuListCallback())
    }

    inner class MenuListCallback: RetrofitCallback<List<Product>> {
        override fun onSuccess(code: Int, result: List<Product>) {
            bestMenuList = result as MutableList<Product>
            bestMenuAdapter.productList = bestMenuList
            bestMenuAdapter.notifyDataSetChanged()
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "메뉴 정보 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }
}