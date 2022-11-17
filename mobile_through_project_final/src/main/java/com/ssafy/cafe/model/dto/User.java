package com.ssafy.cafe.model.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private String id;
    private String name;
    private String pass;
    private Integer stamps;
    private List<Stamp> stampList = new ArrayList<>();
    
    
    @Builder
    public User(String id, String name, String pass, Integer stamps) {
        super();
        this.id = id;
        this.name = name;
        this.pass = pass;
        this.stamps = stamps;
    }
    
    public User(String id, String name, String pass) {
        super();
        this.id = id;
        this.name = name;
        this.pass = pass;
    }
}
