package net.lyof.phantasm.mixin;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.SpikeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpikeFeature.class)
public abstract class EndSpikeFeatureMixin {
    @Inject(method = "placeSpike", at = @At("TAIL"))
    public void randomizeObsidian(ServerLevelAccessor accessor, RandomSource source, SpikeConfiguration configuration, SpikeFeature.EndSpike endSpike, CallbackInfo ci) {
        int i = endSpike.getRadius();
        for(BlockPos blockpos : BlockPos.betweenClosed(new BlockPos(endSpike.getCenterX() - i, accessor.getMinBuildHeight(), endSpike.getCenterZ() - i), new BlockPos(endSpike.getCenterX() + i, endSpike.getHeight() + 10, endSpike.getCenterZ() + i))) {
            if (blockpos.distToLowCornerSqr(endSpike.getCenterX(), blockpos.getY(), endSpike.getCenterZ()) <= (double)(i * i + 1) && blockpos.getY() < endSpike.getHeight()) {
                double crying = (blockpos.getY() - 60) / (endSpike.getHeight() - 60d);
                BlockState state = accessor.getBlockState(blockpos);
                if (state.is(Blocks.OBSIDIAN) && ConfigEntries.improveEndSpires) {
                    if (crying > 0 && Math.random() < crying * crying)
                        state = Blocks.CRYING_OBSIDIAN.defaultBlockState();
                    else if (Math.random() < 0.2)
                        state = Math.random() < 0.5 ? ModBlocks.POLISHED_OBSIDIAN.get().defaultBlockState() : ModBlocks.POLISHED_OBSIDIAN_BRICKS.get().defaultBlockState();
                    accessor.setBlock(blockpos,state,3);
                }
            }
        }
    }
}
