package com.project.service.db;


import com.project.service.core.tier.OrderCountStrategy;
import com.project.service.core.tier.TierQualificationStrategy;
import com.project.service.core.tier.TotalSpendStrategy;
import com.project.service.model.entity.Benefit;
import com.project.service.model.entity.Subscription;
import com.project.service.model.entity.User;
import com.project.service.model.enums.TierLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    @Getter
    private static final DataStore instance = new DataStore();
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, Subscription> subscriptions = new ConcurrentHashMap<>();
    private final Map<TierLevel, List<Benefit>> tierBenefits = new ConcurrentHashMap<>();
    private final Map<TierLevel, List<TierQualificationStrategy>> tierRules = new ConcurrentHashMap<>();

    private DataStore() {
        initializeDefaults();
    }

    private void initializeDefaults() {
        List<TierQualificationStrategy> silverRules = new ArrayList<>();
        silverRules.add(new OrderCountStrategy(5));
        silverRules.add(new TotalSpendStrategy(500.0));
        tierRules.put(TierLevel.SILVER, silverRules);

        List<TierQualificationStrategy> goldRules = new ArrayList<>();
        goldRules.add(new TotalSpendStrategy(1000.0));
        tierRules.put(TierLevel.GOLD, goldRules);

        List<TierQualificationStrategy> platinumRules = new ArrayList<>();
        platinumRules.add(new TotalSpendStrategy(5000.0));
        tierRules.put(TierLevel.PLATINUM, platinumRules);

        tierBenefits.put(TierLevel.SILVER, Collections.singletonList(new Benefit("Free Delivery", 0)));
        tierBenefits.put(TierLevel.GOLD, Arrays.asList(new Benefit("Free Delivery", 0), new Benefit("10% Discount", 10)));
        tierBenefits.put(TierLevel.PLATINUM, Arrays.asList(new Benefit("Priority Support", 0), new Benefit("20% Discount", 20)));
    }

    public void saveUser(User user) {
        users.put(user.getUserId(), user);
    }

    public User getUser(String id) {
        return users.get(id);
    }

    public void saveSubscription(Subscription sub) {
        subscriptions.put(sub.getUserId(), sub);
    }

    public Subscription getSubscription(String userId) {
        return subscriptions.get(userId);
    }

    public List<TierQualificationStrategy> getTierRules(TierLevel tier) {
        return tierRules.getOrDefault(tier, Collections.emptyList());
    }

    public List<Benefit> getBenefits(TierLevel tier) {
        return tierBenefits.getOrDefault(tier, Collections.emptyList());
    }
}