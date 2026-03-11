package net.lyof.phantasm.mixin.client;

import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionSpecialEffects.class)
public class DimensionSpecialEffectsMixin {
    @Inject(method = "forceBrightLightmap", at = @At("HEAD"), cancellable = true)
    public void unbrightenEnd(CallbackInfoReturnable<Boolean> cir) {
        if ((DimensionSpecialEffects) (Object) this instanceof DimensionSpecialEffects.EndEffects)
            cir.setReturnValue(!ConfigEntries.darkEnd);
    }
}