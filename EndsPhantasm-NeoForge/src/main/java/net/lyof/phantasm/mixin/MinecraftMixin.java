package net.lyof.phantasm.mixin;

import net.lyof.phantasm.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow @Nullable public LocalPlayer player;

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    public void charmAttack(CallbackInfoReturnable<Boolean> cir) {
        if (this.player != null && this.player.hasEffect(ModEffects.CHARM))
            cir.setReturnValue(false);
    }

    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
    public void charmItemUse(CallbackInfo ci) {
        if (this.player != null && this.player.hasEffect(ModEffects.CHARM))
            ci.cancel();
    }

    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    public void charmBlockBreaking(CallbackInfo ci) {
        if (this.player != null && this.player.hasEffect(ModEffects.CHARM))
            ci.cancel();
    }
}