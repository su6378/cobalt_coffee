package com.ssafy.cobaltcoffee.util

//import com.ssafy.smartstore.api.CommentApi
//import com.ssafy.smartstore.api.OrderApi
//import com.ssafy.smartstore.api.ProductApi
//import com.ssafy.smartstore.api.UserApi
import com.ssafy.cobaltcoffee.config.ApplicationClass
import com.ssafy.cobaltcoffee.service.ProductService
import com.ssafy.cobaltcoffee.service.UserService

class RetrofitUtil {
    companion object{
//        val commentService = ApplicationClass.retrofit.create(CommentApi::class.java)
//        val orderService = ApplicationClass.retrofit.create(OrderApi::class.java)
        val productService = ApplicationClass.retrofit.create(ProductService::class.java)
        val userService = ApplicationClass.retrofit.create(UserService::class.java)
    }
}