package com.project.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String userId;
    private String name;

    @Builder.Default
    private List<Order> orderHistory = new ArrayList<>();

    public void addOrder(Order order) {
        this.orderHistory.add(order);
    }
}
