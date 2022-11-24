package com.ssafy.cobaltcoffee.dialog

import android.app.Dialog
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.cobaltcoffee.adapter.CartAdapter
import com.ssafy.cobaltcoffee.adapter.OrderCouponAdapter
import com.ssafy.cobaltcoffee.databinding.ActivityPayBinding
import com.ssafy.cobaltcoffee.databinding.DialogLoginBinding
import com.ssafy.cobaltcoffee.databinding.DialogLogoutBinding
import com.ssafy.cobaltcoffee.databinding.DialogOrderCouponBinding
import com.ssafy.cobaltcoffee.dto.CouponDetail
import com.ssafy.cobaltcoffee.util.CommonUtils

private const val TAG = "OrderCouponDialog_코발트"
class OrderCouponDialog(private val context : AppCompatActivity, private val couponList : MutableList<CouponDetail>, private val totalPrice : Int) {
    private lateinit var binding: DialogOrderCouponBinding
    private val dialog = Dialog(context)

    private lateinit var listener : OrderCouponOKClickedListener
    private lateinit var orderCouponAdapter: OrderCouponAdapter

    fun show() : Int {
        var result = totalPrice
        binding = DialogOrderCouponBinding.inflate(context.layoutInflater)

        couponList.add(0,CouponDetail(0,0,"선택하지 않음",0,"",false))
        binding.apply {
            orderCouponAdapter = OrderCouponAdapter(couponList)
            orderCouponAdapter.setItemClickListener(object : OrderCouponAdapter.ItemClickListener{
                override fun onClick(view: View, position: Int, couponId: Int) {
                    Toast.makeText(context,"${couponList[position].couponName}",Toast.LENGTH_SHORT).show()
                    var price = totalPrice - (totalPrice * couponList[position].discountRate / 100)
                    Log.d(TAG, "onClick1: $price")
                    listener.onOKClicked(price)
                    dialog.dismiss()
                    result = price
                    Log.d(TAG, "onClick2: $result")
                }
            })

            orderCouponRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = orderCouponAdapter
                //원래의 목록위치로 돌아오게함
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dialog.setContentView(binding.root)     //다이얼로그에 사용할 xml 파일을 불러옴
        dialog.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.window?.setLayout(width, height)

        //ok 버튼 동작
        binding.okBtn.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
        Log.d(TAG, "show: $result")
        return result
    }

    fun setOnOKClickedListener(listener: () -> Unit){
        this.listener = object: OrderCouponOKClickedListener {
            override fun onOKClicked(price: Int) {
                listener()
            }
        }
    }

    interface OrderCouponOKClickedListener {
        fun onOKClicked(price : Int)
    }
}