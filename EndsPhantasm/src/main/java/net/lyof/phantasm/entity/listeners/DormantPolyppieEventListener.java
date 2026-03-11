package net.lyof.phantasm.entity.listeners;


import net.lyof.phantasm.block.entities.DormantPolyppieBlockEntity;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

public record DormantPolyppieEventListener(DormantPolyppieBlockEntity source) implements GameEventListener {

    @Override
    public PositionSource getListenerSource() {
        return new BlockPositionSource(this.source.getBlockPos());
    }

    @Override
    public int getListenerRadius() {
        return 8;
    }

    @Override
    public boolean handleGameEvent(ServerLevel world, GameEvent event, GameEvent.Context emitter, Vec3 vec3) {
        if (event.is(ModTags.GameEvents.DORMANT_POLYPPIE_CAN_LISTEN)) {
            float chance = emitter.sourceEntity() instanceof PolyppieEntity ? 0.02f : 0.01f;
            if (world.random.nextFloat() < chance)
                world.blockEvent(this.source.getBlockPos(), this.source.getBlockState().getBlock(), 0, 0);
            return true;
        }
        return false;
    }
}
