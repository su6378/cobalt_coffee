package com.ssafy.cafe.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CouponDetail {
    Integer couponId;
    Integer couponTypeId;
    String couponName;
    Integer discountRate;
    String userId;
    Boolean isUse;
    
    @Builder
    public CouponDetail(Integer couponId, Integer couponTypeId, String couponName, Integer discountRate, String userId,
            Boolean isUse) {
        super();
        this.couponId = couponId;
        this.couponTypeId = couponTypeId;
        this.couponName = couponName;
        this.discountRate = discountRate;
        this.userId = userId;
        this.isUse = isUse;
    }
}
