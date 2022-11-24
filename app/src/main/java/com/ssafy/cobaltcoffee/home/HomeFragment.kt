package com.ssafy.cobaltcoffee.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.forms.sti.progresslitieigb.ProgressLoadingIGB
import com.forms.sti.progresslitieigb.finishLoadingIGB
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.adapter.BestMenuAdapter
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.databinding.FragmentHomeBinding
import com.ssafy.cobaltcoffee.dto.Product
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.home.order.ProductActivity
import com.ssafy.cobaltcoffee.repository.ProductRepository
import com.ssafy.cobaltcoffee.repository.UserRepository
import com.ssafy.cobaltcoffee.start.StartActivity
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel

private const val TAG = "HomeFragment_코발트"
class HomeFragment : Fragment() {
    private lateinit var homeActivity: HomeActivity
    private lateinit var binding: FragmentHomeBinding

    private var bestMenuList: MutableList<Product> = mutableListOf()
    private lateinit var bestMenuAdapter: BestMenuAdapter

    private val userViewModel: UserViewModel by activityViewModels()

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

        //로딩창 생성 Lottie
        ProgressLoadingIGB.startLoadingIGB(requireContext()){
            message = " "
            srcLottieJson = R.raw.loading_home
            hight = 800 // Optional
            width = 800 // Optional
            sizeTextMessage = 14f
        }

        init()

        binding.homeBanner.setOnClickListener { //흑임자 라떼 커피 메뉴 상세 페이지로 이동
            homeActivity.detailPage(userViewModel.currentUser)
        }


    }

    private fun init() {

        //유저 정보 갱신
        getUserInfo()

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
                    startActivity(Intent(context, ProductActivity::class.java).apply {
                        putExtra("product", bestMenuList[position])
                        putExtra("user",userViewModel.currentUser)
                    })
                }
            })

            homeRv.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = bestMenuAdapter
                //원래의 목록위치로 돌아오게함
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            }
        }

        //이미지 슬라이더
        val imageList = ArrayList<SlideModel>() // Create image list

        imageList.add(SlideModel(R.drawable.banner4))
        imageList.add(SlideModel(R.drawable.banner5))
        imageList.add(SlideModel(R.drawable.banner6))

        binding.imageSlider.setImageList(imageList,ScaleTypes.FIT)

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
            userViewModel.currentUser = Gson().fromJson(jsonString["user"].toString(), object : TypeToken<User>() {}.type)
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "유저 정보 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
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
            //lottie animation 종료
            Handler(Looper.getMainLooper()).postDelayed({
                homeActivity.finishLoadingIGB()
            },500L) //1초 동안 지속
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "메뉴 정보 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }
}