package com.project.service.model.plan;

public record BasicPlan(String name, int months, double basePrice) implements PlanType {
    @Override
    public String getName() { return name; }

    @Override
    public int getMonths() { return months; }

    @Override
    public double getBasePrice() { return basePrice; }

    @Override
    public String toString() { return name; }
}
