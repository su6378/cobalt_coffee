package com.ssafy.cobaltcoffee.util

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
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

    //textview 특정 text 컬러나 스타일 바꾸기
    fun setSpannableText(text: String, content : String) : SpannableString{
        val spannableString = SpannableString(content)
        val start: Int = content.indexOf(text)
        val end = start + text.length

        when(text){
            "BLUE" ->  spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#0080FF")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            "BRONZE" -> spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#A65E44")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            "SILVER" ->spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#AEAEAE")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            "GOLD" ->spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#CFB53B")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            else -> spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#1E3AD9")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
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