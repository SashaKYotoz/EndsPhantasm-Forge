package net.lyof.phantasm.mixin;

import net.lyof.phantasm.effect.ModEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    public void charmCursor(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if (((Entity) (Object) this) instanceof LivingEntity living && living.hasEffect(ModEffects.CHARM))
            ci.cancel();
    }
}