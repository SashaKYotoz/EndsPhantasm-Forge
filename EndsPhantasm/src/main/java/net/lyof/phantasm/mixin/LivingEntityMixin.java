package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.entities.ChallengeRuneBlockEntity;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.entity.access.Challenger;
import net.lyof.phantasm.entity.access.Corrosive;
import net.lyof.phantasm.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Challenger, Corrosive {
    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }
    @Shadow @org.jetbrains.annotations.Nullable
    public abstract MobEffectInstance getEffect(MobEffect effect);
    @Shadow public abstract boolean hasEffect(MobEffect effect);

    @Shadow public abstract boolean addEffect(MobEffectInstance effect);

    @WrapOperation(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    public void keepOblifruit(ItemStack instance, int amount, Operation<Void> original) {
        if (instance.is(ModItems.OBLIFRUIT.get()) && Math.random() < 0.05 && instance.getCount() < instance.getMaxStackSize()) {
            instance.grow(1);
            return;
        }
        if (instance.is(ModItems.OBLIFRUIT.get()) && Math.random() < 0.4) return;
        original.call(instance, amount);
    }

    @ModifyReturnValue(method = "getDamageAfterMagicAbsorb", at = @At("RETURN"))
    public float applyVulnerability(float original) {
        if (!this.hasEffect(ModEffects.CORROSION.get())) return original;

        int i = this.getEffect(ModEffects.CORROSION.get()).getAmplifier() + 1;
        return original * (1 + (float) ConfigEntries.corrosionMultiplier * i);
    }

    @WrapMethod(method = "hurt")
    public boolean doAttackEffects(DamageSource source, float amount, Operation<Boolean> original) {
        if (source.getEntity() instanceof LivingEntity attacker && attacker.hasEffect(ModEffects.CHARM.get()))
            return false;

        boolean v = original.call(source, amount);

        if (source.getEntity() instanceof Corrosive corrosive && corrosive.isCorrosive())
            this.addEffect(new MobEffectInstance(ModEffects.CORROSION.get(), ConfigEntries.corrosiveDuration, ConfigEntries.corrosiveAmplifier));
        return v;
    }

    @Inject(method = "getSpeed()F", at = @At("HEAD"), cancellable = true)
    public void charmMovementSpeed(CallbackInfoReturnable<Float> cir) {
        if (this.hasEffect(ModEffects.CHARM.get())) cir.setReturnValue(0f);
    }

    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    public void charmMovement(CallbackInfo ci) {
        if (this.hasEffect(ModEffects.CHARM.get())) ci.cancel();
    }

    @Inject(method = "die", at = @At("HEAD"))
    public void countRuneKill(DamageSource damageSource, CallbackInfo ci) {
        if (this.getChallengeRune() != null && this.getTags().contains(Phantasm.MOD_ID + ".challenge"))
            this.getChallengeRune().progress();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tickCorrosive(CallbackInfo ci) {
        if (this.corrosiveTicks > 0)
            this.corrosiveTicks--;
    }


    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void writeRune(CompoundTag nbt, CallbackInfo ci) {
        if (this.getChallengeRune() != null) {
            nbt.putInt(Phantasm.MOD_ID + "_RuneX", this.getChallengeRune().getBlockPos().getX());
            nbt.putInt(Phantasm.MOD_ID + "_RuneY", this.getChallengeRune().getBlockPos().getY());
            nbt.putInt(Phantasm.MOD_ID + "_RuneZ", this.getChallengeRune().getBlockPos().getZ());
        }

        if (this.corrosiveTicks > 0)
            nbt.putInt(Phantasm.MOD_ID + "_CorrosiveTicks", this.corrosiveTicks);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void readRune(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains(Phantasm.MOD_ID + "_RuneX"))
            this.challengePos = new BlockPos(nbt.getInt(Phantasm.MOD_ID + "_RuneX"),
                    nbt.getInt(Phantasm.MOD_ID + "_RuneY"),
                    nbt.getInt(Phantasm.MOD_ID + "_RuneZ"));

        if (nbt.contains(Phantasm.MOD_ID + "_CorrosiveTicks"))
            this.corrosiveTicks = nbt.getInt(Phantasm.MOD_ID + "_CorrosiveTicks");
    }


    @Unique
    private ChallengeRuneBlockEntity challengeRune = null;
    @Unique private BlockPos challengePos = null;

    @Override
    public Player asPlayer() { return null; }

    @Override
    public @Nullable ChallengeRuneBlockEntity getChallengeRune() {
        if (this.challengeRune == null && this.challengePos != null
                && this.level().getBlockEntity(this.challengePos) instanceof ChallengeRuneBlockEntity rune)
            this.challengeRune = rune;
        return this.challengeRune;
    }

    @Override
    public void setChallengeRune(ChallengeRuneBlockEntity rune) {
        this.challengeRune = rune;
        this.challengePos = rune == null ? null : rune.getBlockPos();
    }


    @Unique private int corrosiveTicks;

    @Override
    public void setCorrosiveTicks(int ticks) {
        this.corrosiveTicks = ticks;
    }

    @Override
    public boolean isCorrosive() {
        return this.corrosiveTicks > 0;
    }
}