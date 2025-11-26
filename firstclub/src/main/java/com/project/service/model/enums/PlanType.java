package com.project.service.model.enums;

public enum PlanType {
    MONTHLY(1, 10.0),
    QUARTERLY(3, 25.0),
    YEARLY(12, 90.0);

    private final int months;
    private final double basePrice;

    PlanType(int months, double basePrice) {
        this.months = months;
        this.basePrice = basePrice;
    }

    public int getMonths() { return months; }
    public double getBasePrice() { return basePrice; }
}