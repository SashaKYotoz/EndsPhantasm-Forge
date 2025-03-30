package net.lyof.phantasm.entities.goals;

import net.lyof.phantasm.Phantasm;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.NavigationFilter;
import java.util.EnumSet;

public class FlyAroundGoal extends Goal {
    public PathfinderMob self;

    public FlyAroundGoal(PathfinderMob entity) {
        this.self = entity;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canContinueToUse() {
        return self.getNavigation().isInProgress();
    }

    public boolean canUse() {
        MoveControl movecontrol = this.self.getMoveControl();
        if (!movecontrol.hasWanted()) {
            return true;
        } else {
            double d0 = movecontrol.getWantedX() - this.self.getX();
            double d1 = movecontrol.getWantedY() - this.self.getY();
            double d2 = movecontrol.getWantedZ() - this.self.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            return d3 < 1.0D || d3 > 3600.0D;
        }
    }

    public void start() {
        RandomSource randomsource = this.self.getRandom();
        double d0 = this.self.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
        double d1 = this.self.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
        double d2 = this.self.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
        this.self.getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);
    }
}