package com.ssafy.cafe.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

import com.ssafy.cafe.model.dto.Product;

public interface ProductService {
    /**
     * 모든 상품 정보를 반환한다.
     * @return
     */
    List<Product> getProductList();
    
    /**
     * 신규 상품 정보를 반환한다.
     * @return
     */
    List<Product> getNewProductList();
    
    /**
     * 인기 상품 정보를 반환한다.
     * @return
     */
    List<Product> getBestProductList();
    
    /**
     * 커피 상품 정보를 반환한다.
     * @return
     */
    List<Product> getCoffeeProductList();
    
    /**
     * 차 상품 정보를 반환한다.
     * @return
     */
    List<Product> getTeaProductList();
    
    /**
     * 쿠키 상품 정보를 반환한다.
     * @return
     */
    List<Product> getCookieProductList();
    
    /**
     * backend 관통 과정에서 추가됨
     * 상품의 정보, 판매량, 평점 정보를 함께 반환
     * @param productId
     * @return
     */
    List<Map<String, Object>> selectWithComment(Integer productId);
}
