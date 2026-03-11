package net.lyof.phantasm.entity.goals;

import net.lyof.phantasm.entity.animations.BehemothAnimation;
import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class BehemothAttackGoal extends MeleeAttackGoal {
    public BehemothEntity self;

    public BehemothAttackGoal(BehemothEntity self, double speed, boolean pauseWhenMobIdle) {
        super(self, speed, pauseWhenMobIdle);
        this.self = self;
    }

    @Override
    public boolean canUse() {
        return this.self.animation == BehemothAnimation.WALKING && super.canUse();
    }
}
