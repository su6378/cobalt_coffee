package com.ssafy.cobaltcoffee.home.order

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.MenuAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.databinding.FragmentMenuBinding
import com.ssafy.cobaltcoffee.dto.Product
import com.ssafy.cobaltcoffee.home.HomeActivity
import okhttp3.internal.immutableListOf

private const val TAG = "MenuFragment_코발트"
class MenuFragment(productType: Int) : Fragment() {
    private lateinit var homeActivity: HomeActivity
    private lateinit var menuFragment: MenuFragment
    private lateinit var binding: FragmentMenuBinding

    private lateinit var productAdapter: ProductAdapter

    private val productType: Int = productType

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
        val productList: MutableList<Product> = mutableListOf<Product>()
        val productTypes: List<String> = mutableListOf<String>("과자", "음료", "차")
        productList.apply {
            for (i in 0 .. 30) {
                when (i % 3) {
                    0 -> this.add(Product(productType + i, "상품이름${productType + i}", productTypes[i % 3], (productType + i) * 1_000,"ICED 카페 모카.png", false, false))
                    1 -> this.add(Product(productType + i, "상품이름${productType + i}", productTypes[i % 3], (productType + i) * 1_000,"ICED 카페 아메리카노.png", true, false))
                    2 -> this.add(Product(productType + i, "상품이름${productType + i}", productTypes[i % 3], (productType + i) * 1_000,"(EX) ICED 버블 흑당 콜드브루.png", false, true))
                }

            }
        }
        
        binding.apply {
            productAdapter = ProductAdapter(requireContext(), productList)
            productAdapter.setOnItemClickListener(object : ProductAdapter.OnItemClickListener {
                override fun onItemClick(v: View, pos: Int) {
                    Log.d(TAG, "onItemClick: ${pos}")
                    Toast.makeText(requireContext(), "${productList[pos].name}", Toast.LENGTH_SHORT).show()
//                    v.product = productList[pos]
//                    mainActivity.openFragment(3, "productId", productId)
                }
            })

            recyclerViewMenu.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = productAdapter
//                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        }
    }
}