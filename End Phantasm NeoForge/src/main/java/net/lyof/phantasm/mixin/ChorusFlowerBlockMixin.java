package net.lyof.phantasm.mixin;

import net.lyof.phantasm.setup.ModTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ChorusFlowerBlock.class, priority = 972)
public class ChorusFlowerBlockMixin {
    @Redirect(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    public boolean canPlaceAtNihilium(BlockState instance, Block block) {
        if (block == Blocks.END_STONE)
            return instance.is(ModTags.Blocks.END_PLANTS_GROWABLE_ON);
        return instance.is(block);
    }
}
