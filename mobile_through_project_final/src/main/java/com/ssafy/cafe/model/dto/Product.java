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
    private String content;
    private Integer kcal;
    
    @Builder
    public Product(Integer id, String name, String type, Integer price, String img, boolean isNew, boolean isBest, String content, Integer kcal) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.img = img;
        this.isNew = isNew;
        this.isBest = isBest;
        this.content = content;
        this.kcal = kcal;
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
