package net.lyof.phantasm.mixin;

import net.lyof.phantasm.setup.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BushBlock.class)
public class PlantBlockMixin {
    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    private void customPlantTypes(BlockState state, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Block self = (Block) (Object) this;
        if (self.defaultBlockState().is(ModTags.Blocks.END_PLANTS)) {
            cir.setReturnValue(state.isFaceSturdy(world, pos.below(), Direction.UP, SupportType.FULL)
                    && state.is(ModTags.Blocks.END_PLANTS_GROWABLE_ON));
            cir.cancel();
        }
    }
}