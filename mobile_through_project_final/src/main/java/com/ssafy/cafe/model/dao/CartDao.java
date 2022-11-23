package com.ssafy.cafe.model.dao;

import java.util.List;

import com.ssafy.cafe.model.dto.Cart;
import com.ssafy.cafe.model.dto.CartDetail;

public interface CartDao {
    List<CartDetail> selectCartList(String userId);
    
    Cart select(Cart cart);
    
    int insert(Cart cart);
    
    int update (Cart cart);
    
    int delete(Integer cartId);
    
    int deleteAll(String userId);
}
