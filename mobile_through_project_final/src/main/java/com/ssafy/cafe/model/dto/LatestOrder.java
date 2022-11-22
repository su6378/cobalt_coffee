package com.ssafy.cafe.model.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LatestOrder {
    String img;
    int orderCnt;
    String userId;
    int orderId;
    String productName;
    Date orderDate;
    char orderCompleted;
    int productPrice;
    String type;
    int totalPrice;
    int productId;
    
    @Builder
    public LatestOrder(String img, int orderCnt, String userId, int orderId, String productName, Date orderDate,
            char orderCompleted, int productPrice, String type, int totalPrice, int productId) {
        super();
        this.img = img;
        this.orderCnt = orderCnt;
        this.userId = userId;
        this.orderId = orderId;
        this.productName = productName;
        this.orderDate = orderDate;
        this.orderCompleted = orderCompleted;
        this.productPrice = productPrice;
        this.type = type;
        this.totalPrice = totalPrice;
        this.productId = productId;
    }
    
}
