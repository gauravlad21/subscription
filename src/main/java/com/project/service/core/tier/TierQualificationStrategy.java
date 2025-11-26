package com.project.service.core.tier;

import com.project.service.model.User;

public interface TierQualificationStrategy {

    boolean isEligible(User user);
}
