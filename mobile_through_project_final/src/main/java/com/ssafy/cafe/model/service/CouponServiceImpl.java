package com.ssafy.cafe.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.cafe.model.dao.CouponDao;
import com.ssafy.cafe.model.dto.Coupon;
import com.ssafy.cafe.model.dto.CouponDetail;
import com.ssafy.cafe.model.dto.CouponType;

@Service
public class CouponServiceImpl implements CouponService {
    
    @Autowired
    CouponDao cDao;
    
    @Override
    public List<CouponDetail> getCouponList(String userId) {
        return cDao.selectByUserId(userId);
    }
    
    @Override
    public Integer check(String userId, Integer couponTypeId) {
        Coupon coupon = new Coupon(0, couponTypeId, userId, false);
        Coupon result = cDao.select(coupon);
        
        if (result == null) return couponTypeId;
        else return 0;
    }
    
    @Override
    @Transactional
    public CouponType addCoupon(Coupon coupon) {
        cDao.insert(coupon);
        return cDao.selectType(coupon.getTypeId());
    }
    
    @Override
    @Transactional
    public void useCoupon(Integer couponId) {
        cDao.update(couponId);
    }

}
