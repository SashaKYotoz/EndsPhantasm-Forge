package net.lyof.phantasm.entities.goals;

import net.lyof.phantasm.entities.custom.BehemothEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
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
    public boolean canContinueToUse() {
        return self.isAngry();
    }

    @Override
    public void tick() {
        if (self.tickCount % 20 == 0 && self.level() instanceof ServerLevel level)
            level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    self.getX(), self.getEyeY(), self.getZ(), 1, 0, 0, 0, 0);
    }
}