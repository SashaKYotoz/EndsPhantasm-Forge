package net.lyof.phantasm.entities.goals;

import net.lyof.phantasm.entities.custom.CrystieEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class CrystieAirMovementGoal extends Goal {
    private final CrystieEntity crystieEntity;
    public CrystieAirMovementGoal(CrystieEntity crystie){
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.crystieEntity = crystie;
    }
    public boolean canUse() {
        return crystieEntity.getTarget() != null && !crystieEntity.getMoveControl().hasWanted();
    }
    @Override
    public boolean canContinueToUse() {
        return crystieEntity.getMoveControl().hasWanted() && crystieEntity.getTarget() != null && crystieEntity.getTarget().isAlive();
    }

    @Override
    public void start() {
        LivingEntity livingentity = crystieEntity.getTarget();
        Vec3 vec3d = livingentity.getEyePosition(1);
        crystieEntity.getMoveControl().setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 3);
    }
    @Override
    public void tick() {
        LivingEntity livingentity = crystieEntity.getTarget();
        if (crystieEntity.getBoundingBox().intersects(livingentity.getBoundingBox())) {
            crystieEntity.doHurtTarget(livingentity);
        } else {
            double d0 = crystieEntity.distanceToSqr(livingentity);
            if (d0 < 32) {
                Vec3 vec3d = livingentity.getEyePosition(1);
                crystieEntity.getMoveControl().setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 3);
            }
        }
    }
}