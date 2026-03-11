package net.lyof.phantasm.entity.custom;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.effect.ModEffects;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;

public class SourSludgeEntity extends Slime {
    private static ParticleOptions particles = null;

    protected float bounceDistance;

    public SourSludgeEntity(EntityType<? extends Slime> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    protected ParticleOptions getParticleType() {
        if (particles == null)
            particles = new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.ACIDIC_MASS.get().defaultBlockState());
        return particles;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader world) {
        return world.isUnobstructed(this) && !world.containsAnyLiquid(this.getBoundingBox());
    }

    @Override
    public void setSize(int size, boolean heal) {
        super.setSize(size, heal);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((3 + size) * size);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2 + size);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.44 + 0.04 * size);
        if (heal) this.setHealth(this.getMaxHealth());
    }

    @Override
    protected boolean isDealsDamage() {
        return this.isEffectiveAi();
    }

    @Override
    protected void decreaseSquish() {
        this.targetSquish *= 0.95f;
    }

    @Override
    protected float getJumpPower() {
        if (this.bounceDistance > 5) {
            float v = (float) (Math.log(this.bounceDistance - 2) * 0.5);
            this.bounceDistance = 0;
            return v;
        }
        return super.getJumpPower() + 0.05f * this.getSize() + 0.025f;
    }

    @Override
    public boolean isSensitiveToWater() {
        return !this.isTiny();
    }

    @Override
    protected float getDamageAfterMagicAbsorb(DamageSource source, float amount) {
        if (source.is(DamageTypes.DROWN)) amount *= 5;
        return super.getDamageAfterMagicAbsorb(source, amount);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean b = super.hurt(source, amount);
        RandomSource random = this.getRandom();
        if (b && source.is(DamageTypes.DROWN) && random.nextFloat() < 0.2) {
            AreaEffectCloud cloud = EntityType.AREA_EFFECT_CLOUD.create(this.level());
            cloud.addEffect(new MobEffectInstance(ModEffects.CORROSION.get(), random.nextIntBetweenInclusive(100, 400), random.nextInt(3)));
            cloud.setOwner(this);
            cloud.setPos(this.getOnPos().getCenter());
            this.level().addFreshEntity(cloud);
        }
        return b;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return super.canBeAffected(effect) && effect.getEffect() != ModEffects.CORROSION.get();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        this.bounceDistance = fallDistance;
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.bounceDistance > 5) {
            Vec3 v = this.getDeltaMovement();
            this.setDeltaMovement(v.x, Math.log(this.bounceDistance - 2) * 0.5, v.z);
            this.hasImpulse = true;
            this.bounceDistance = 0;
        }
    }

    @Override
    public void doEnchantDamageEffects(LivingEntity attacker, Entity target) {
        super.doEnchantDamageEffects(attacker, target);
        if (target instanceof LivingEntity living) {
            int duration = 60, amplifier = Math.random() < 0.2 ? 1 : 0;
            MobEffectInstance effect = living.getEffect(ModEffects.CORROSION.get());
            if (effect != null) {
                duration += effect.getDuration();
                amplifier += effect.getAmplifier();
            }
            living.addEffect(new MobEffectInstance(ModEffects.CORROSION.get(), duration, amplifier));
        }
    }
}
