package net.lyof.phantasm.block.entities;

import net.lyof.phantasm.entity.listeners.DormantPolyppieEventListener;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;

public class DormantPolyppieBlockEntity extends BlockEntity implements GameEventListener.Holder<DormantPolyppieEventListener> {
    protected final DormantPolyppieEventListener listener;

    public DormantPolyppieBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DORMANT_POLYPPIE.get(), pos, state);
        this.listener = new DormantPolyppieEventListener(this);
    }

    @Override
    public DormantPolyppieEventListener getListener() {
        return this.listener;
    }
}