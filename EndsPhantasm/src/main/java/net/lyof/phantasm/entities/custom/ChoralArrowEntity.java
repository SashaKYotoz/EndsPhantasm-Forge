package net.lyof.phantasm.entities.custom;

import net.lyof.phantasm.block.custom.SubwooferBlock;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.entities.ModEntities;
import net.lyof.phantasm.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChoralArrowEntity extends Arrow {
    public int lifetime = 0;
    public boolean shotByCrossbow = false;

    public ChoralArrowEntity(EntityType<? extends Arrow> type, Level level) {
        super(type, level);
    }

    public static ChoralArrowEntity create(Level level, LivingEntity shooter) {
        ChoralArrowEntity arrow = create(level, shooter.getX(), shooter.getEyeY() - 0.10000000149011612D, shooter.getZ());
        arrow.setOwner(shooter);
        return arrow;
    }

    public static ChoralArrowEntity create(Level level, double x, double y, double z) {
        ChoralArrowEntity arrow = new ChoralArrowEntity(ModEntities.CHORAL_ARROW.get(), level);
        arrow.setPos(x, y, z);
        return arrow;
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        if (!this.shotByCrossbow) {
            super.doPostHurtEffects(entity);
            entity.addEffect(new MobEffectInstance(ModEffects.CHARM.get(), 40, 0));
        }
    }

    @Override
    public ItemStack getPickupItem() {
        return ModItems.CHORAL_ARROW.get().getDefaultInstance();
    }

    @Override
    protected float getWaterInertia() {
        return 0.2f;
    }

    @Override
    public void shoot(double x, double y, double z, float speed, float divergence) {
        super.shoot(x, y, z, speed * 0.75f, divergence * 4f);
    }

    @Override
    public boolean isCritArrow() {
        return false;
    }

    public static boolean isUsingCrossbow(LivingEntity shooter) {
        return shooter.getMainHandItem().getItem() instanceof CrossbowItem
                || (shooter.getOffhandItem().getItem() instanceof CrossbowItem && !(shooter.getMainHandItem().getItem() instanceof BowItem));
    }

    @Override
    public void tick() {
        if (this.lifetime == 0)
            this.shotByCrossbow = this.getOwner() != null && this.getOwner() instanceof LivingEntity living &&
                    isUsingCrossbow(living);
        if (this.shotByCrossbow && this.lifetime > 0)
            this.discard();

        if (this.shotByCrossbow && this.getOwner() != null && this.lifetime == 0) {
            Level level = this.level();
            Entity shooter = this.getOwner();

            Vec3 direction = this.getDeltaMovement().normalize();
            Vec3 position = this.position();
            int range = ConfigEntries.subwooferRange * 2;

            List<UUID> affected = new ArrayList<>();

            if (!shooter.isCrouching()) {
                shooter.setDeltaMovement(direction.scale(-1.5).add(0, 0.1, 0));
                shooter.hurtMarked = true;
                shooter.fallDistance = 0;
                affected.add(shooter.getUUID());
            }

            BlockPos pos;
            for (int i = 0; i < range; i++) {
                pos = new BlockPos((int) Math.round(position.x - 0.5), (int) Math.round(position.y - 0.5), (int) Math.round(position.z - 0.5));
                List<Entity> entities = level.getEntities(shooter, new AABB(pos).inflate(1), SubwooferBlock::canPush);

                level.addAlwaysVisibleParticle(ParticleTypes.SONIC_BOOM,
                        position.x, position.y, position.z,
                        0, 0, 0);
                level.playSound(null, pos, SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 0.2f, 1.5f);

                for (Entity e : entities) {
                    if (affected.contains(e.getUUID())) continue;

                    affected.add(e.getUUID());
                    e.hurt(shooter.damageSources().arrow(this, shooter), 6);
                    e.setDeltaMovement(direction.scale(2.5).add(0, 0.4, 0));
                    e.hurtMarked = true;
                }

                if (level.getBlockState(pos).is(BlockTags.OCCLUDES_VIBRATION_SIGNALS)) break;

                position = position.add(direction);
            }
        }

        if (!this.shotByCrossbow)
            super.tick();

        if (!this.shotByCrossbow && this.level().isClientSide() && !this.inGround) {
            this.level().addParticle(ParticleTypes.NOTE,
                    this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D),
                    this.lifetime / 20f, 0, 0);
        }
        this.lifetime++;
    }
}