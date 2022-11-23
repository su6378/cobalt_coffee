package com.ssafy.cafe.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Cart {
    private Integer id;
    private String userId;
    private Integer productId;
    private Integer quantity;
    
    @Builder
    public Cart(Integer id, String userId, Integer productId, Integer quantity) {
        super();
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
