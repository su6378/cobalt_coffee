package com.ssafy.cobaltcoffee.util

import com.ssafy.cobaltcoffee.config.ApplicationClass
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {

    //천단위 콤마
    fun makeComma(num: Int): String {
        var comma = DecimalFormat("#,###")
        return "${comma.format(num)} 원"
    }

    fun getFormattedString(date:Date): String {
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분")
        dateFormat.timeZone = TimeZone.getTimeZone("Seoul/Asia")

        return dateFormat.format(date)
    }

//    // 시간 계산을 통해 완성된 제품인지 확인
//    fun isOrderCompleted(orderDetail: OrderDetailResponse): String {
//        return if( checkTime(orderDetail.orderDate.time))  "주문완료" else "진행 중.."
//    }
//
//    // 시간 계산을 통해 완성된 제품인지 확인
//    fun isOrderCompleted(order: LatestOrderResponse): String {
//        return if( checkTime(order.orderDate.time))  "주문완료" else "진행 중.."
//    }

    private fun checkTime(time:Long):Boolean{
        val curTime = (Date().time+60*60*9*1000)

        return (curTime - time) > ApplicationClass.ORDER_COMPLETED_TIME
    }
}