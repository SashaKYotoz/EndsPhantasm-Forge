package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.effect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class PombBlock extends FallingBlock {
    public PombBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    }

    @Override
    public int getDustColor(BlockState state, BlockGetter getter, BlockPos pos) {
        return state.getMapColor(getter, pos).col;
    }

    @Override
    protected void falling(FallingBlockEntity entity) {
        super.falling(entity);
        entity.disableDrop();
    }

    @Override
    public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity entity) {
        for (Entity entity1 : level.getEntities(null, new AABB(pos).inflate(5))) {
            if (entity1 instanceof LivingEntity living) {
                living.hurt(living.damageSources().dragonBreath(), (float) ConfigEntries.fallingPombDamage);
                living.addEffect(new MobEffectInstance(ModEffects.CORROSION.get(), ConfigEntries.fallingPombDuration, ConfigEntries.fallingPombAmplifier));
                entity1.setDeltaMovement(entity1.position().add(0, 0.5, 0).subtract(pos.getCenter()).normalize().scale(3));
            }
        }

        if (level.isClientSide()) level.explode(null, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                2, Level.ExplosionInteraction.NONE);
        Block.dropResources(this.defaultBlockState(), level, pos, null, null, ItemStack.EMPTY);
        level.playSound(null, pos, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.BLOCKS);
        super.onBrokenAfterFall(level, pos, entity);
    }
}