package com.ssafy.cafe.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Comment {
    private Integer id;
    private String userId;
    private Integer productId;
    private Double rating;
    private String comment;
    
    @Builder
    public Comment(Integer id, String userId, Integer productId, Double rating, String comment) {
        super();
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
    }
    public Comment( String userId, Integer productId, Double rating, String comment) {
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
    }
    
}