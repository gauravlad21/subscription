package com.project.service.model.tier;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = BasicTier.class)
public interface TierLevel extends Comparable<TierLevel> {
    String getName();
    int getPriority();

    @Override
    default int compareTo(TierLevel other) {
        return Integer.compare(this.getPriority(), other.getPriority());
    }
}
