package com.ssafy.cafe.model.dao;

import java.util.List;

import com.ssafy.cafe.model.dto.Coupon;
import com.ssafy.cafe.model.dto.CouponDetail;

public interface CouponDao {
    List<CouponDetail> selectByUserId(String userId);
    
    int insert(Coupon coupon);
    
    int update(Integer couponId);
}
