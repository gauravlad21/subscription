package com.project.service;

import com.project.service.model.constants.Catalog;
import com.project.service.core.membership.MembershipService;
import com.project.service.db.DataStore;
import com.project.service.model.Order;
import com.project.service.model.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("Initializing FirstClub Backend...");

        MembershipService service = new MembershipService();
        DataStore db = DataStore.getInstance();

        // 1. Create Users
        User alice = User.builder().userId("u1").name("Alice").build();
        User bob = User.builder().userId("u2").name("Bob").build();
        User charlie = User.builder().userId("u3").name("Charlie").build();

        db.saveUser(alice);
        db.saveUser(bob);
        db.saveUser(charlie);

        // 2. Scenario: Charlie subscribes
        log.info("\n\n\n\n--- SCENARIO 1: Basic Subscription ---");
        service.subscribe("u3", Catalog.MONTHLY); // Using Catalog constant
        service.printUserStatus("u3");

        // 3. Scenario: Alice subscribes & tries to upgrade (Fail)
        log.info("\n\n\n\n--- SCENARIO 2: Upgrade Validation (Fail) ---");
        service.subscribe("u1", Catalog.YEARLY);
        service.upgradeTier("u1", Catalog.GOLD);

        // 4. Scenario: Alice spends money & Upgrades
        log.info("\n\n\n\n--- SCENARIO 3: Activity & Upgrade ---");
        alice.addOrder(Order.builder().orderId("o1").amount(1200.0).build());
        service.upgradeTier("u1", Catalog.GOLD);
        service.printUserStatus("u1");

        // 5. Scenario: Bob Order Count Upgrade
        log.info("\n\n\n\n--- SCENARIO 4: Order Count Criterion ---");
        service.subscribe("u2", Catalog.QUARTERLY);
        for(int i=0; i<6; i++) {
            bob.addOrder(Order.builder().orderId("b"+i).amount(10.0).build());
        }
        service.upgradeTier("u2", Catalog.SILVER);
        service.printUserStatus("u2");
    }
}