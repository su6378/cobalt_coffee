package com.ssafy.cafe.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {
    private Integer id;
    private String name;
    private String type;
    private Integer price;
    private String img;
    private boolean isNew;
    private boolean isBest;
    
    @Builder
    public Product(Integer id, String name, String type, Integer price, String img, boolean isNew, boolean isBest) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.img = img;
        this.isNew = isNew;
        this.isBest = isBest;
    }
    
    public Product(String name, String type, Integer price, String img, boolean isNew, boolean isBest) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.img = img;
        this.isNew = isNew;
        this.isBest = isBest;
    }
}
