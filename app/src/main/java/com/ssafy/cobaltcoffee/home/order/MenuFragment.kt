package com.ssafy.cobaltcoffee.home.order

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.databinding.FragmentMenuBinding
import com.ssafy.cobaltcoffee.dto.Product
import com.ssafy.cobaltcoffee.home.HomeActivity
import com.ssafy.cobaltcoffee.repository.ProductRepository
import com.ssafy.smartstore.util.RetrofitCallback

private const val TAG = "MenuFragment_코발트"
class MenuFragment(productType: Int) : Fragment() {
    private lateinit var homeActivity: HomeActivity
    private lateinit var menuFragment: MenuFragment
    private lateinit var binding: FragmentMenuBinding

    private lateinit var productAdapter: ProductAdapter

    private var productList: MutableList<Product> = mutableListOf()
    private val productType = productType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    })
                }
            })

            recyclerViewMenu.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = productAdapter
//                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        }
    }

    inner class MenuListCallback: RetrofitCallback<List<Product>> {
        override fun onSuccess(code: Int, result: List<Product>) {
            productList = result as MutableList<Product>
            productAdapter.products = productList
            productAdapter.notifyDataSetChanged()
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "메뉴 정보 불러오는 중 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }
}