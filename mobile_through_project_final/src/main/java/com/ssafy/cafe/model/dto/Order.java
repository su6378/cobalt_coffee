package com.ssafy.cafe.model.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Order {
    private Integer id;
    private String userId;
    private String orderTable;
    private Date orderTime;

    private Character completed;
    private List<OrderDetail> details ;
    private Stamp stamp;
    
    @Builder
    public Order(Integer id, String userId, String orderTable, Date orderTime, Character complited) {
        super();
        this.id = id;
        this.userId = userId;
        this.orderTable = orderTable;
        this.orderTime = orderTime;
        this.completed = complited;
    }

    public Order(String userId, String orderTable, Date orderTime, Character complited) {
        this.userId = userId;
        this.orderTable = orderTable;
        this.orderTime = orderTime;
        this.completed = complited;
    }
    
}
