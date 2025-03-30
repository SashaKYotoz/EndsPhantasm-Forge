package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.EndPlatformFeature;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EndPlatformFeature.class)
public class EndPlatformFeatureMixin {
    @WrapMethod(method = "createEndPlatform")
    private static void noPlatform(ServerLevelAccessor level, BlockPos pos, boolean dropBlocks, Operation<Void> original) {
        BlockPos.MutableBlockPos mutable = ServerLevel.END_SPAWN_POINT.mutable();
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                for (int k = -1; k < 3; ++k) {
                    BlockState blockState = k == -1 ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.AIR.defaultBlockState();
                    level.setBlock(mutable.set(ServerLevel.END_SPAWN_POINT).move(Direction.DOWN, 2).move(j, k, i), blockState, 3);
                }
            }
        }
    }
}