package net.lyof.phantasm.mixin;

import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DragonFireball.class)
public class DragonFireballMixin extends AbstractHurtingProjectile {
    protected DragonFireballMixin(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "onHit", at = @At("TAIL"))
    public void explode(HitResult hitResult, CallbackInfo ci) {
        if (!(hitResult instanceof EntityHitResult entityHitResult) || !this.ownedBy(entityHitResult.getEntity()) && ConfigEntries.explosiveDragonFireballs)
            this.level().explode((DragonFireball) (Object) this, this.getX(), this.getY() + 0.5, this.getZ(),
                    3, true, Level.ExplosionInteraction.MOB);
    }
}