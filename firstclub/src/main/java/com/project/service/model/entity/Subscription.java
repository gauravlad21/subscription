package com.project.service.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.service.model.enums.PlanType;
import com.project.service.model.enums.TierLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    @Builder.Default
    private String subscriptionId = UUID.randomUUID().toString();

    private String userId;
    private PlanType planType;
    private TierLevel tier;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate = LocalDateTime.now();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryDate;

    @Builder.Default
    private boolean isActive = true;

    @JsonIgnore
    public boolean isValid() {
        return isActive && LocalDateTime.now().isBefore(expiryDate);
    }
}
