package com.project.service.model.constants;

import com.project.service.model.plan.BasicPlan;
import com.project.service.model.plan.PlanType;
import com.project.service.model.tier.BasicTier;
import com.project.service.model.tier.TierLevel;

public class Catalog {
    public static final TierLevel STANDARD = new BasicTier("Standard", 0);
    public static final TierLevel SILVER   = new BasicTier("Silver", 1);
    public static final TierLevel GOLD     = new BasicTier("Gold", 2);
    public static final TierLevel PLATINUM = new BasicTier("Platinum", 3);

    public static final PlanType MONTHLY   = new BasicPlan("Monthly", 1, 10.0);
    public static final PlanType QUARTERLY = new BasicPlan("Quarterly", 3, 25.0);
    public static final PlanType YEARLY    = new BasicPlan("Yearly", 12, 90.0);

}