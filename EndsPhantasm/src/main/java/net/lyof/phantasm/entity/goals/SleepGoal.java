package net.lyof.phantasm.entity.goals;

import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class SleepGoal extends Goal {
    public BehemothEntity self;

    public SleepGoal(BehemothEntity self) {
        this.self = self;
    }

    @Override
    public boolean canUse() {
        return !self.isAngry();
    }

    @Override
    public boolean isInterruptable() {
        return self.isAngry();
    }
}