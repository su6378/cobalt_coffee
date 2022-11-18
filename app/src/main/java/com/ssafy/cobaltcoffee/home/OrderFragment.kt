package com.ssafy.cobaltcoffee.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.cobaltcoffee.databinding.FragmentOrderBinding
import com.ssafy.cobaltcoffee.home.order.MenuFragment
import com.ssafy.cobaltcoffee.home.order.ProductListActivity

class OrderFragment : Fragment() {
    private lateinit var homeActivity: HomeActivity
    private lateinit var binding: FragmentOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        binding.apply {
            btnOrder.setOnClickListener {
                val intent: Intent = Intent(context, ProductListActivity::class.java)
                startActivity(intent)
            }
            btnCart.setOnClickListener {
                Toast.makeText(context, "장바구니", Toast.LENGTH_SHORT).show()
            }
        }
    }
}