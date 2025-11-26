package com.project.service.core.tier;

import com.project.service.model.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderCountStrategy implements TierQualificationStrategy {

    private int requiredCount;

    @Override
    public boolean isEligible(User user) {
        return user.getOrderHistory().size() >= requiredCount;
    }
}
