package com.ssafy.cobaltcoffee.service

import com.ssafy.cobaltcoffee.dto.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    // 전체 상품의 목록을 반환한다.
    @GET("/rest/product/")
    fun getProductList(): Call<List<Product>>

    // 신규 상품의 목록을 반환한다.
    @GET("/rest/product/new")
    fun getNewProductList(): Call<List<Product>>

    // 인기 상품의 목록을 반환한다.
    @GET("/rest/product/best")
    fun getBestProductList(): Call<List<Product>>

    // 커피 상품의 목록을 반환한다.
    @GET("/rest/product/coffee")
    fun getCoffeeProductList() : Call<List<Product>>

    // 차 상품의 목록을 반환한다.
    @GET("/rest/product/tea")
    fun getTeaProductList(): Call<List<Product>>

    // 쿠키 상품의 목록을 반환한다.
    @GET("/rest/product/cookie")
    fun getCookieProductList(): Call<List<Product>>

    // 상품 정보 반환
    @GET("/rest/product/{productId}")
    fun getProductWithComments(@Path("productId") productId: String): Call<List<Map<String, Any>>>
}