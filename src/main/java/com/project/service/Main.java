package com.project.service;

import com.project.service.core.membership.MembershipService;
import com.project.service.db.DataStore;
import com.project.service.model.entity.Order;
import com.project.service.model.entity.User;
import com.project.service.model.enums.PlanType;
import com.project.service.model.enums.TierLevel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("Initializing FirstClub Backend...");

        MembershipService service = new MembershipService();
        DataStore db = DataStore.getInstance();

        User alice = User.builder().userId("u1").name("Alice").build();
        User bob = User.builder().userId("u2").name("Bob").build();
        User charlie = User.builder().userId("u3").name("Charlie").build();

        db.saveUser(alice);
        db.saveUser(bob);
        db.saveUser(charlie);

        log.info("\n--- SCENARIO 1: Basic Subscription ---");
        service.subscribe("u3", PlanType.MONTHLY);
        service.printUserStatus("u3");

        log.info("\n--- SCENARIO 2: Upgrade Validation (Fail) ---");
        service.subscribe("u1", PlanType.YEARLY);
        service.upgradeTier("u1", TierLevel.GOLD);

        log.info("\n--- SCENARIO 3: Activity & Upgrade ---");
        alice.addOrder(Order.builder().orderId("o1").amount(1200.0).build());
        service.upgradeTier("u1", TierLevel.GOLD);
        service.printUserStatus("u1");

        log.info("\n--- SCENARIO 4: Order Count Criterion ---");
        service.subscribe("u2", PlanType.QUARTERLY);
        for(int i=0; i<6; i++) {
            bob.addOrder(Order.builder().orderId("b"+i).amount(10.0).build());
        }
        service.upgradeTier("u2", TierLevel.SILVER);
        service.printUserStatus("u2");
    }
}