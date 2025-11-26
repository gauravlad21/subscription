package com.project.service.core.tier;

import com.project.service.model.entity.User;

public interface TierQualificationStrategy {

    boolean isEligible(User user);
}
