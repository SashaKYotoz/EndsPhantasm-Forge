package net.lyof.phantasm.mixin;

import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.item.ModItems;
import net.minecraft.core.Holder;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract boolean hasEffect(Holder<MobEffect> effect);

    @Shadow @Nullable public abstract MobEffectInstance getEffect(Holder<MobEffect> effect);

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Redirect(method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"))
    public void keepOblifruit(ItemStack instance, int amount, LivingEntity entity) {
        if (instance.is(ModItems.OBLIFRUIT.get()) && Math.random() < 0.05 && instance.getCount() < instance.getMaxStackSize()) {
            instance.grow(1);
            return;
        }
        if (instance.is(ModItems.OBLIFRUIT.get()) && Math.random() < 0.4) return;
        instance.shrink(1);
    }

    @Inject(method = "getDamageAfterMagicAbsorb", at = @At("RETURN"), cancellable = true)
    public void applyVulnerability(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (!this.hasEffect(ModEffects.CORROSION)) return;

        int i = this.getEffect(ModEffects.CORROSION).getAmplifier() + 1;
        cir.setReturnValue(amount * (1 + 0.2f * i));
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void cancelDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getEntity() instanceof LivingEntity attacker && attacker.hasEffect(ModEffects.CHARM))
            cir.setReturnValue(false);
    }

    @Inject(method = "getSpeed", at = @At("HEAD"), cancellable = true)
    public void charmMovementSpeed(CallbackInfoReturnable<Float> cir) {
        if (this.hasEffect(ModEffects.CHARM)) cir.setReturnValue(0f);
    }

    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    public void charmMovement(CallbackInfo ci) {
        if (this.hasEffect(ModEffects.CHARM)) ci.cancel();
    }
}
