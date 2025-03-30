package net.lyof.phantasm.entities.goals;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entities.custom.CrystieEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class DiveBombGoal extends Goal {
    public CrystieEntity self;
    public int tick = 0;

    public DiveBombGoal(CrystieEntity entity) {
        this.self = entity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return self.isAngry && getNearestPlayer(32) != null;
    }

    @Override
    public void start() {
        Player target =getNearestPlayer(32);
        if (target != null) {
            this.tick = 0;
            Phantasm.log("trying to attack " + target);
            self.getNavigation().moveTo(self.getNavigation().createPath(target, 16), 1);
        }
        else this.stop();
    }

    @Override
    public void stop() {
        self.getNavigation().stop();
        self.isAngry = false;
        this.tick = 0;
    }

    @Override
    public void tick() {
        this.tick++;
        if (this.tick > 100) this.stop();
        Player target = getNearestPlayer(32);
        if (target == null) {
            this.stop();
            return;
        }
        self.getNavigation().moveTo(self.getNavigation().createPath(target, 16), 1);
        if (self.distanceTo(target) < 3) {
            this.stop();
            self.explode();
        }
    }
    public Player getNearestPlayer(int radiusOfLooking) {
        final Vec3 center = new Vec3(self.getX(), self.getY(), self.getZ());
        List<Entity> entityList = self.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(radiusOfLooking), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(center))).toList();
        for (Entity entityiterator : entityList) {
            if (entityiterator instanceof Player player)
                return player;
        }
        return null;
    }
}
