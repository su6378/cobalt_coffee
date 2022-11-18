package com.ssafy.cobaltcoffee.home.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.cobaltcoffee.databinding.FragmentMenuBinding
import com.ssafy.cobaltcoffee.home.HomeActivity

class MenuFragment(productType: Int) : Fragment() {
    private lateinit var homeActivity: HomeActivity
    private lateinit var menuFragment: MenuFragment
    private lateinit var binding: FragmentMenuBinding

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
        binding.productType.text = productType.toString()




    }
}