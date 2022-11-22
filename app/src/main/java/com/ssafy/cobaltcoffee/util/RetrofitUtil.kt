package com.ssafy.cobaltcoffee.util

import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.service.CouponService
import com.ssafy.cobaltcoffee.service.OrderService
import com.ssafy.cobaltcoffee.service.ProductService
import com.ssafy.cobaltcoffee.service.UserService

class RetrofitUtil {
    companion object{
//        val commentService = ApplicationClass.retrofit.create(CommentService::class.java)
        val orderService = ApplicationClass.retrofit.create(OrderService::class.java)
        val productService = ApplicationClass.retrofit.create(ProductService::class.java)
        val userService = ApplicationClass.retrofit.create(UserService::class.java)
        val couponService = ApplicationClass.retrofit.create(CouponService::class.java)
    }
}