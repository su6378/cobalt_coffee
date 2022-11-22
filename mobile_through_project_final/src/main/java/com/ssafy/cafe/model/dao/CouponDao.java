package com.ssafy.cafe.model.dao;

import java.util.List;

import com.ssafy.cafe.model.dto.Coupon;

public interface CouponDao {
    List<Coupon> selectByUserId(String userId);
    
    int insert(Coupon coupon);
    
    int update(Integer couponId);
}
