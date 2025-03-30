package net.lyof.phantasm.entities.goals;

import net.lyof.phantasm.Phantasm;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class AvoidGroundGoal extends Goal {
    public PathfinderMob self;

    public AvoidGroundGoal(PathfinderMob entity) {
        this.self = entity;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return !self.level().getBlockState((self.blockPosition().below())).isAir();
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        self.getNavigation().moveTo(self.getX(), self.getY() + 3, self.getZ(), 1);
        Phantasm.log(self.getNavigation().getTargetPos());
        //self.setVelocity(self.getVelocity().add(0, 0.1, 0));
    }

    @Override
    public void stop() {
        super.stop();
        //self.getNavigation().stop();
    }
}
