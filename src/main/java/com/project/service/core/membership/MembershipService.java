package com.project.service.core.membership;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.service.model.constants.Catalog;
import com.project.service.db.DataStore;
import com.project.service.model.Subscription;
import com.project.service.model.User;
import com.project.service.model.plan.PlanType;
import com.project.service.model.tier.TierLevel;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class MembershipService {
    private final DataStore db = DataStore.getInstance();
    private final Map<String, ReentrantLock> userLocks = new ConcurrentHashMap<>();
    private final ObjectMapper jsonMapper;

    public MembershipService() {
        this.jsonMapper = new ObjectMapper();
        this.jsonMapper.registerModule(new JavaTimeModule());
        this.jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    private ReentrantLock getLock(String userId) {
        return userLocks.computeIfAbsent(userId, k -> new ReentrantLock());
    }

    public Subscription subscribe(String userId, PlanType plan) {
        ReentrantLock lock = getLock(userId);
        lock.lock();
        try {
            User user = db.getUser(userId);
            if (user == null) throw new IllegalArgumentException("User not found");

            Subscription existing = db.getSubscription(userId);
            if (existing != null && existing.isValid()) {
                throw new IllegalStateException("User already has an active subscription.");
            }

            Subscription newSub = Subscription.builder()
                    .userId(userId)
                    .planType(plan)
                    .tier(Catalog.STANDARD) // Use Catalog constant
                    .startDate(LocalDateTime.now())
                    .expiryDate(LocalDateTime.now().plusMonths(plan.getMonths()))
                    .isActive(true)
                    .build();

            db.saveSubscription(newSub);
            logJson("Subscribed", newSub);
            return newSub;
        } catch (Exception e) {
            System.err.println("Error subscribing: " + e.getMessage());
            return null;
        } finally {
            lock.unlock();
        }
    }

    public void upgradeTier(String userId, TierLevel targetTier) {
        ReentrantLock lock = getLock(userId);
        lock.lock();
        try {
            Subscription sub = db.getSubscription(userId);
            if (sub == null || !sub.isValid()) {
                throw new IllegalStateException("No active subscription found.");
            }

            // Using Comparable interface logic defined in TierLevel
            if (sub.getTier().compareTo(targetTier) >= 0) {
                log.info(">> INFO: User already at/above " + targetTier.getName());
                return;
            }

            User user = db.getUser(userId);
            boolean eligible = db.getTierRules(targetTier).stream()
                    .anyMatch(strategy -> strategy.isEligible(user));

            if (eligible) {
                sub.setTier(targetTier);
                logJson("Upgraded Tier", sub);
            } else {
                log.info(">> FAIL: " + user.getName() + " not eligible for " + targetTier.getName());
            }
        } catch (Exception e) {
            System.err.println("Error upgrading: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public void printUserStatus(String userId) {
        try {
            User u = db.getUser(userId);
            Subscription s = db.getSubscription(userId);

            Map<String, Object> statusMap = new LinkedHashMap<>();
            statusMap.put("user", u.getName());
            statusMap.put("orders", u.getOrderHistory().size());

            if (s != null && s.isValid()) {
                statusMap.put("subscription", s);
                statusMap.put("currentBenefits", db.getBenefits(s.getTier()));
            } else {
                statusMap.put("subscription", "None/Expired");
            }

            log.info("\n--- User Status Report (" + userId + ") ---");
            log.info(jsonMapper.writeValueAsString(statusMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logJson(String action, Object obj) {
        try {
            log.info(">> " + action + ": " + jsonMapper.writeValueAsString(obj));
        } catch (Exception e) {
            log.info(">> " + action + ": " + obj);
        }
    }
}
