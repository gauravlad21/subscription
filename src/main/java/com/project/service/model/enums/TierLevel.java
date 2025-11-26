package com.project.service.model.enums;

public enum TierLevel {
    STANDARD(0),
    SILVER(1),
    GOLD(2),
    PLATINUM(3);

    private final int priority;

    TierLevel(int priority) {
        this.priority = priority;
    }

    public int getPriority() { return priority; }
}
