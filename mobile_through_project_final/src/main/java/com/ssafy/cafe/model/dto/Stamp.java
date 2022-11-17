package com.ssafy.cafe.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Stamp {
    private Integer id;
    private String userId;
    private Integer orderId;
    private Integer quantity;
    
    @Builder
    public Stamp(Integer id, String userId, Integer orderId, Integer quantity) {
        super();
        this.id = id;
        this.userId = userId;
        this.orderId = orderId;
        this.quantity = quantity;
    }
    
    public Stamp(String userId, Integer orderId, Integer quantity) {
        this.userId = userId;
        this.orderId = orderId;
        this.quantity = quantity;
    }
}
