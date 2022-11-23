package com.ssafy.cafe.model.service;

import java.util.List;

import com.ssafy.cafe.model.dto.Cart;
import com.ssafy.cafe.model.dto.CartDetail;

public interface CartService {
    /**
     * 장바구니에 있는 상품을 조회한다.
     * @param userId
     * @return
     */
    List<CartDetail> getList(String userId);
    
    /**
     * 장바구니에 상품이 있는지 확인한다.
     * @param cart
     * @return
     */
    Boolean check(String userId, Integer productId);
    
    /**
     * 장바구니에 상품을 담는다.
     * @param cart
     */
    void addCart(Cart cart);
    
    /**
     * 상품을 담은 개수를 변경한다.
     * @param cart
     */
    void updateCart(Cart cart);
    
    /**
     * 장바구니에 담긴 상품 하나를 지운다.
     * @param cartId
     */
    void deleteCart(Integer cartId);
    
    /**
     * 사용자에 해당하는 장바구니에 담긴 상품 전체를 지운다.
     * @param userId
     */
    void deleteAllCart(String userId);
}
