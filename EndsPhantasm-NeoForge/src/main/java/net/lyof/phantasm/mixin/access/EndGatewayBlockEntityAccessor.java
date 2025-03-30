package net.lyof.phantasm.mixin.access;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TheEndGatewayBlockEntity.class)
public interface EndGatewayBlockEntityAccessor {
    @Invoker(value = "findExitPosition")
    static BlockPos getExitPos(Level level, BlockPos origin) {
        throw new AssertionError();
    }
}
