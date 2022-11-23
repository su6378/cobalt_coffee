package com.ssafy.cafe.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartDetail {
    private Integer cartId;
    private String userId;
    private String productId;
    private String productQty;
    private String productType;
    private String productImage;
    private String productPrice;
    private String totalPrice;
    
    @Builder
    public CartDetail(Integer cartId, String userId, String productId, String productQty, String productType,
            String productImage, String productPrice, String totalPrice) {
        super();
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.productQty = productQty;
        this.productType = productType;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.totalPrice = totalPrice;
    }
}
