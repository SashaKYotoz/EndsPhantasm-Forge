package net.lyof.phantasm.entities.custom;

import net.lyof.phantasm.client.particles.ModParticles;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.entities.ModEntities;
import net.lyof.phantasm.entities.animations.BehemothAnimation;
import net.lyof.phantasm.entities.goals.BehemothAttackGoal;
import net.lyof.phantasm.entities.goals.SleepGoal;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class BehemothEntity extends Monster implements Enemy {
    public static final EntityDimensions SLEEPING_DIMENSIONS = EntityDimensions.fixed(2F, 1f);
    public static final EntityDimensions STANDARD_DIMENSIONS = EntityDimensions.fixed(0.95f, 1.95f);
    public int angryTicks = 0;
    public int animTicks = 0;
    public BehemothAnimation animation = BehemothAnimation.SLEEPING;
    public static int MAX_ANGRY_TICKS = 600;

    public BehemothEntity(EntityType<? extends BehemothEntity> type, Level level) {
        super(ModEntities.BEHEMOTH.get(), level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SleepGoal(this));
        this.goalSelector.addGoal(1, new BehemothAttackGoal(this, 1, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60)
                .add(Attributes.ATTACK_DAMAGE, 15)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10);
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return this.animation == BehemothAnimation.SLEEPING ? SLEEPING_DIMENSIONS : STANDARD_DIMENSIONS;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return super.getHurtSound(source);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return super.getDeathSound();
    }

    public boolean isAngry() {
        return this.getTarget() != null;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!source.isDirect()) return false;
        if (source.getEntity() instanceof Player player && !player.isCreative() && !player.isSpectator()) {
            this.setTarget(player);
            this.angryTicks = MAX_ANGRY_TICKS;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target == null && this.isAngry()) this.setAnimation(BehemothAnimation.WAKING_DOWN);
        else if (target != null && !this.isAngry()) {
            this.setAnimation(BehemothAnimation.WAKING_UP);
            this.playSound(SoundEvents.ENDER_DRAGON_GROWL, 1, 1);
        }

        super.setTarget(target);

        if (target == null) this.angryTicks = 0;
        else this.angryTicks = MAX_ANGRY_TICKS;
    }

    public void setAnimation(BehemothAnimation anim) {
        if (anim != this.animation) this.animTicks = 0;
        this.animation = anim;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 20 == 0)
            refreshDimensions();
        this.animTicks++;
        if (this.animation.maxTime > 0 && this.animTicks > this.animation.maxTime) {
            if (this.isAngry())
                this.setAnimation(BehemothAnimation.WALKING);
            else
                this.setAnimation(BehemothAnimation.SLEEPING);
            //else this.setAnimation(BehemothAnimation.IDLE);
        }

        if (this.angryTicks > 0) this.angryTicks--;
        else if (this.isAngry()) this.setTarget(null);
        else if (this.tickCount % 20 == 0) {
            Player player = this.level().getNearestPlayer(this, ConfigEntries.behemothAggroRange);
            if (player != null && !player.isCreative() && !player.isSpectator()
                    && (!player.isCrouching() || this.distanceTo(player) < ConfigEntries.behemothAggroRangeSneaking))
                this.setTarget(player);
        }
        if (this.getTarget() != null && (this.getTarget().distanceTo(this) > 16 || !this.getTarget().isAlive()))
            this.setTarget(null);

        if (!this.isAngry() && this.tickCount % 20 == 0) {
            this.playSound(SoundEvents.SNIFFER_SNIFFING, 2, 1);
            if (this.level().isClientSide() && this.getRandom().nextInt(2) == 0)
                this.level().addParticle(ModParticles.ZZZ.get(),
                        this.getX() - Math.sin(-this.getYRot() * Math.PI / 180), this.getY() + 0.3,
                        this.getZ() - Math.cos(this.getYRot() * Math.PI / 180), 0, 0.05, 0);
        }
    }
}