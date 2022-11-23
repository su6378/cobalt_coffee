package com.ssafy.cafe.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.cafe.model.dao.CartDao;
import com.ssafy.cafe.model.dto.Cart;
import com.ssafy.cafe.model.dto.CartDetail;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartDao cDao;
    
    @Override
    public List<CartDetail> getList(String userId) {
        return cDao.selectCartList(userId);
    }
    
    @Override
    public Boolean check(String userId, Integer productId) {
        Cart c = cDao.select(new Cart(0, userId, productId, 0));
        if (c == null) return true;
        else return false;
    }

    @Override
    public void addCart(Cart cart) {
        cDao.insert(cart);
    }

    @Override
    public void updateCart(Cart cart) {
        cDao.update(cart);
    }

    @Override
    public void deleteCart(Integer cartId) {
        cDao.delete(cartId);
    }

    @Override
    public void deleteAllCart(String userId) {
        cDao.deleteAll(userId);
    }

}
