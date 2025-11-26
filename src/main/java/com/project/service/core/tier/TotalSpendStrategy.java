package com.project.service.core.tier;

import com.project.service.model.Order;
import com.project.service.model.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TotalSpendStrategy implements TierQualificationStrategy {
    private double requiredAmount;

    @Override
    public boolean isEligible(User user) {
        double total = user.getOrderHistory().stream()
                .mapToDouble(Order::getAmount)
                .sum();
        return total >= requiredAmount;
    }
}