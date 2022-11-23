package com.ssafy.cafe.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Coupon {
    private Integer id;
    private Integer typeId;
    private String userId;
    private Boolean isUse;
    
    @Builder
    public Coupon(Integer id, Integer typeId, String userId, Boolean isUse) {
        super();
        this.id = id;
        this.typeId = typeId;
        this.userId = userId;
        this.isUse = isUse;
    }
}
