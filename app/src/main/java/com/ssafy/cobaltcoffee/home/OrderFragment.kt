package com.ssafy.cobaltcoffee.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.adapter.ImageSliderAdapter
import com.ssafy.cobaltcoffee.databinding.FragmentOrderBinding
import com.ssafy.cobaltcoffee.home.order.ProductListActivity


class OrderFragment : Fragment() {
    private lateinit var homeActivity: HomeActivity
    private lateinit var binding: FragmentOrderBinding

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

    private fun init() {
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
}