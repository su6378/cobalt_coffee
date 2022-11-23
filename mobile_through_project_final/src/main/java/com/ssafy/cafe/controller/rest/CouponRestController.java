package com.ssafy.cafe.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cafe.model.dto.Coupon;
import com.ssafy.cafe.model.dto.CouponDetail;
import com.ssafy.cafe.model.service.CouponService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/coupon")
@CrossOrigin("*")
public class CouponRestController {
    @Autowired
    CouponService cService;
    
    @GetMapping("/")
    @ApiOperation(value="사용자의 쿠폰 목록을 반환한다.", response = List.class)
    public ResponseEntity<List<CouponDetail>> getCouponList(String userId){
        return new ResponseEntity<List<CouponDetail>>(cService.getCouponList(userId), HttpStatus.OK);
    }
    
    @PostMapping("/new")
    @ApiOperation(value="사용자에게 쿠폰을 발급한다", response = Boolean.class)
    public Boolean addCoupon(@RequestBody Coupon coupon) {
        cService.addCoupon(coupon);
        return true;
    }
    
    @PutMapping("/use/{couponId}")
    @ApiOperation(value="사용자의 쿠폰을 사용처리한다.", response = Boolean.class)
    public Boolean useCoupon(@PathVariable Integer couponId){
        cService.useCoupon(couponId);
        return true;
    }
}
