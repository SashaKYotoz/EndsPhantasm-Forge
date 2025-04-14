package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.item.ModItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    public abstract boolean hasEffect(MobEffect effect);

    @Shadow
    @Nullable
    public abstract MobEffectInstance getEffect(MobEffect effect);

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @WrapOperation(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    public void keepOblifruit(ItemStack instance, int i, Operation<Void> original) {
        if (instance.is(ModItems.OBLIFRUIT.get()) && Math.random() < 0.05 && instance.getCount() < instance.getMaxStackSize()) {
            instance.grow(1);
            return;
        }
        if (instance.is(ModItems.OBLIFRUIT.get()) && Math.random() < 0.4) return;
        instance.shrink(1);
    }

    @Inject(method = "getDamageAfterMagicAbsorb", at = @At("RETURN"), cancellable = true)
    public void applyVulnerability(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (!this.hasEffect(ModEffects.CORROSION.get())) return;

        int i = this.getEffect(ModEffects.CORROSION.get()).getAmplifier() + 1;
        cir.setReturnValue(amount * (1 + 0.2f * i));
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void cancelDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getEntity() instanceof LivingEntity attacker && attacker.hasEffect(ModEffects.CHARM.get()))
            cir.setReturnValue(false);
    }

    @Inject(method = "getSpeed", at = @At("HEAD"), cancellable = true)
    public void charmMovementSpeed(CallbackInfoReturnable<Float> cir) {
        if (this.hasEffect(ModEffects.CHARM.get())) cir.setReturnValue(0f);
    }

    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    public void charmMovement(CallbackInfo ci) {
        if (this.hasEffect(ModEffects.CHARM.get())) ci.cancel();
    }
}
