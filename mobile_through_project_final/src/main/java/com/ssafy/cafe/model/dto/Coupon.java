package com.ssafy.cafe.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Coupon {
    private Integer id;
    private String userId;
    private String name;
    private Integer discountRate;
    private Boolean isUse;
    
    @Builder
    public Coupon(Integer id, String userId, String name, Integer discountRate, Boolean isUse) {
        super();
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.discountRate = discountRate;
        this.isUse = isUse;
    }
}
