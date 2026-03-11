package net.lyof.phantasm.mixin;

import net.lyof.phantasm.block.entities.ChallengeRuneBlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystal.class)
public abstract class EndCrystalMixin extends Entity {
    public EndCrystalMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "isPickable", at = @At("HEAD"), cancellable = true)
    public void preventHit(CallbackInfoReturnable<Boolean> cir) {
        if (this.level().getBlockEntity(this.blockPosition().below()) instanceof ChallengeRuneBlockEntity challengeRune
                && challengeRune.isChallengeRunning())
            cir.setReturnValue(false);
    }
}