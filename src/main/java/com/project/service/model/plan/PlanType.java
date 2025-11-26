package com.project.service.model.plan;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = BasicPlan.class)
public interface PlanType {
    String getName();
    int getMonths();
    double getBasePrice();
}