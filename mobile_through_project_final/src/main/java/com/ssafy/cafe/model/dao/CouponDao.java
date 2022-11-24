package com.ssafy.cafe.model.dao;

import java.util.List;

import com.ssafy.cafe.model.dto.Coupon;
import com.ssafy.cafe.model.dto.CouponDetail;
import com.ssafy.cafe.model.dto.CouponType;

public interface CouponDao {
    List<CouponDetail> selectByUserId(String userId);
    
    List<CouponDetail> selectByUserIdCanUse(String userId);
    
    Coupon select(Coupon coupon);
    
    CouponType selectType(Integer couponTypeId);
    
    int insert(Coupon coupon);
    
    int update(Integer couponId);
}
