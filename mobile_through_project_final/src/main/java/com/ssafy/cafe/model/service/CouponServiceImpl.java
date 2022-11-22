package com.ssafy.cafe.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.cafe.model.dao.CouponDao;
import com.ssafy.cafe.model.dto.Coupon;

public class CouponServiceImpl implements CouponService {
    
    @Autowired
    CouponDao cDao;
    
    @Override
    public List<Coupon> getCouponList(String userId) {
        return cDao.selectByUserId(userId);
    }
    
    @Override
    @Transactional
    public void addCoupon(Coupon coupon) {
        cDao.insert(coupon);
    }
    
    @Override
    @Transactional
    public void useCoupon(Integer couponId) {
        cDao.update(couponId);
    }

}
