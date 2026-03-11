package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.block.entities.DormantPolyppieBlockEntity;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DormantPolyppieBlock extends DirectionalBlock implements EntityBlock {
    public DormantPolyppieBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int type, int data) {
        if (type == 0) {
            world.destroyBlock(pos, false);
            PolyppieEntity polyppie = ModEntities.POLYPPIE.get().create(world);
            polyppie.setPos(pos.getCenter());
            world.addFreshEntity(polyppie);
            return true;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(type, data);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DormantPolyppieBlockEntity(pos, state);
    }
}