package com.project.service.model.tier;


public record BasicTier(String name, int priority) implements TierLevel {
    @Override
    public String getName() { return name; }

    @Override
    public int getPriority() { return priority; }

    @Override
    public String toString() { return name; }
}
