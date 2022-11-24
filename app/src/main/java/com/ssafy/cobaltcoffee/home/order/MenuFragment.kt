package com.ssafy.cobaltcoffee.home.order

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.forms.sti.progresslitieigb.ProgressLoadingIGB
import com.forms.sti.progresslitieigb.finishLoadingIGB
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.FragmentMenuBinding
import com.ssafy.cobaltcoffee.dto.Product
import com.ssafy.cobaltcoffee.home.HomeActivity
import com.ssafy.cobaltcoffee.repository.ProductRepository
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel

private const val TAG = "MenuFragment_코발트"
class MenuFragment(productType: Int) : Fragment() {
    private lateinit var productListActivity: ProductListActivity
    private lateinit var menuFragment: MenuFragment
    private lateinit var binding: FragmentMenuBinding

    private lateinit var productAdapter: ProductAdapter

    private var productList: MutableList<Product> = mutableListOf()
    private val productType = productType

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        productListActivity = context as ProductListActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {

        //로딩창 생성 Lottie
        ProgressLoadingIGB.startLoadingIGB(requireContext()){
            message = " "
            srcLottieJson = R.raw.loading_home
            hight = 800 // Optional
            width = 800 // Optional
            sizeTextMessage = 14f
        }

        when (productType) {
            0 -> ProductRepository.get().getNewProductList(MenuListCallback())
            1 -> ProductRepository.get().getBestProductList(MenuListCallback())
            2 -> ProductRepository.get().getCoffeeProductList(MenuListCallback())
            3 -> ProductRepository.get().getTeaProductList(MenuListCallback())
            4 -> ProductRepository.get().getCookieProductList(MenuListCallback())
        }

        binding.apply {
            productAdapter = ProductAdapter(requireContext(), productList)
            productAdapter.setOnItemClickListener(object : ProductAdapter.OnItemClickListener {
                override fun onItemClick(v: View, pos: Int) {
                    startActivity(Intent(context, ProductActivity::class.java).apply {
                        putExtra("product", productList[pos])
                        putExtra("user",userViewModel.currentUser)
                    })
                }
            })

            recyclerViewMenu.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                addItemDecoration(DividerItemDecoration(requireContext(), 1))
                adapter = productAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        }
    }

    inner class MenuListCallback: RetrofitCallback<List<Product>> {
        override fun onSuccess(code: Int, result: List<Product>) {
            productList = result as MutableList<Product>
            productAdapter.products = productList
            productAdapter.notifyDataSetChanged()
            //lottie animation 종료
            Handler(Looper.getMainLooper()).postDelayed({
                productListActivity.finishLoadingIGB()
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