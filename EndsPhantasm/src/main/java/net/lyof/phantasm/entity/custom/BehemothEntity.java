package net.lyof.phantasm.entity.custom;

import io.netty.buffer.Unpooled;
import net.lyof.phantasm.client.particles.ModParticles;
import net.lyof.phantasm.entity.animations.BehemothAnimation;
import net.lyof.phantasm.entity.goals.BehemothAttackGoal;
import net.lyof.phantasm.entity.goals.SleepGoal;
import net.lyof.phantasm.entity.listeners.BehemothEventListener;
import net.lyof.phantasm.setup.ModPackets;
import net.lyof.phantasm.setup.packets.BehemothAwakesPacket;
import net.lyof.phantasm.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;

public class BehemothEntity extends Monster implements Enemy {
    public static final EntityDimensions SLEEPING_DIMENSIONS = EntityDimensions.fixed(1.5f, 1f);
    public static final EntityDimensions STANDARD_DIMENSIONS = EntityDimensions.fixed(0.95f, 1.95f);
    public int angryTicks = 0;
    public int animTicks = 0;
    public BehemothAnimation animation = BehemothAnimation.SLEEPING;
    public static int MAX_ANGRY_TICKS = 600;

    public final DynamicGameEventListener<BehemothEventListener> listener = new DynamicGameEventListener<>(new BehemothEventListener(this));

    public BehemothEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = Enemy.XP_REWARD_LARGE;
    }

    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> callback) {
        if (this.level() instanceof ServerLevel level)
            callback.accept(this.listener, level);
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
    public EntityDimensions getDimensions(Pose pose) {
        return this.animation == BehemothAnimation.SLEEPING ? SLEEPING_DIMENSIONS : STANDARD_DIMENSIONS;
    }

    @Override
    public float getStepHeight() {
        return 1f;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SNIFFER_SNIFFING;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.BEHEMOTH_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.BEHEMOTH_DYING;
    }

    @Override
    public float getVoicePitch() {
        return 0.85f;
    }

    public boolean isAngry() {
        return this.getTarget() != null;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.isIndirect()) return false;
        if (source.getEntity() instanceof Player player && !player.isCreative() && !player.isSpectator())
            this.setTarget(player);
        return super.hurt(source, amount);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (!this.level().isClientSide()) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeInt(this.getId());
            buf.writeInt(target == null ? 0 : target.getId());
            for (ServerPlayer player : tracking((ServerLevel) this.level(), new ChunkPos(this.getOnPos())))
                ModPackets.sendToPlayer(new BehemothAwakesPacket(buf), player);
        }

        if (target == null && this.isAngry()) this.setAnimation(BehemothAnimation.WAKING_DOWN);
        else if (target != null && !this.isAngry()) {
            this.setAnimation(BehemothAnimation.WAKING_UP);
            this.playSound(ModSounds.BEHEMOTH_AMBIENT, 0.9f, 1.5F);
        }

        super.setTarget(target);

        if (target == null) this.angryTicks = 0;
        else this.angryTicks = MAX_ANGRY_TICKS;
    }

    public Collection<ServerPlayer> tracking(ServerLevel world, ChunkPos pos) {
        Objects.requireNonNull(world, "The world cannot be null");
        Objects.requireNonNull(pos, "The chunk pos cannot be null");

        return world.getChunkSource().chunkMap.getPlayers(pos, false);
    }

    public void setAnimation(BehemothAnimation anim) {
        if (anim != this.animation) this.animTicks = 0;
        this.animation = anim;
        this.refreshDimensions();
    }

    public static boolean canMobSpawn(EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        BlockPos blockPos = pos.below();
        return spawnReason == MobSpawnType.SPAWNER || world.getBlockState(blockPos).isValidSpawn(world, blockPos, type);
    }

    @Override
    public void tick() {
        if (this.firstTick) this.refreshDimensions();
        super.tick();

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
        if (this.getTarget() != null && (this.getTarget().distanceTo(this) > 16 || !this.getTarget().isAlive()))
            this.setTarget(null);

        if (!this.isAngry() && this.tickCount % 20 == 0) {
            this.playSound(SoundEvents.SNIFFER_SNIFFING, 1, 1);
            if (this.level().isClientSide() && this.getRandom().nextInt(2) == 0)
                this.level().addParticle(ModParticles.ZZZ.get(),
                        this.getX() - Math.sin(-this.getYRot() * Math.PI / 180), this.getY() + 0.3,
                        this.getZ() - Math.cos(this.getYRot() * Math.PI / 180), 0, 0.05, 0);
        }
    }
}