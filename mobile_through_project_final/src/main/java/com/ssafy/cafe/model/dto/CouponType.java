package com.ssafy.cafe.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CouponType {
    Integer id;
    String name;
    Integer discountRate;
    
    @Builder
    public CouponType(Integer id, String name, Integer discountRate) {
        super();
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
    }
}
