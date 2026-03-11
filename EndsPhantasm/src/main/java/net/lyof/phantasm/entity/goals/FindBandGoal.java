package net.lyof.phantasm.entity.goals;

import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class FindBandGoal extends Goal {
    private static final TargetingConditions VALID_MATE_PREDICATE = TargetingConditions.forNonCombat().range(8.0).ignoreInvisibilityTesting();
    protected final PolyppieEntity polyppie;
    protected final Level level;
    @Nullable protected PolyppieEntity target;
    private int timer;
    private final double speed;

    public FindBandGoal(PolyppieEntity polyppie, double speed) {
        this.polyppie = polyppie;
        this.level = polyppie.level();
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        if (!this.polyppie.getBand().canMove())
            return false;
        this.target = this.findMate();
        return this.target != null;
    }

    public boolean canContinueToUse() {
        return this.target != null && this.target.isAlive() && this.timer < 60;
    }

    public void stop() {
        this.target = null;
        this.timer = 0;
    }

    public void tick() {
        this.polyppie.getLookControl().setLookAt(this.target, 10.0F, (float) this.polyppie.getMaxHeadXRot());
        this.polyppie.getNavigation().moveTo(this.target, this.speed);
        this.timer++;
        if (this.timer >= this.adjustedTickDelay(60) && this.polyppie.distanceToSqr(this.target) < 9) {
            this.polyppie.joinBand(this.target);
            this.target = null;
        }
    }

    @Nullable
    private PolyppieEntity findMate() {
        List<? extends PolyppieEntity> list = this.level.getNearbyEntities(PolyppieEntity.class, VALID_MATE_PREDICATE, this.polyppie, this.polyppie.getBoundingBox().inflate(8.0));
        double d = 32;
        PolyppieEntity target = null;

        for (PolyppieEntity it : list) {
            if (!it.getBand().contains(this.polyppie.getId()) && this.polyppie.distanceToSqr(it) < d) {
                target = it;
                d = this.polyppie.distanceToSqr(it);
            }
        }

        return target;
    }
}
