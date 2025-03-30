package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ChorusPlantBlock.class, priority = 972)
public class ChorusPlantBlockMixin {
    @WrapOperation(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    public boolean canPlaceAtNihilium(BlockState instance, Block block, Operation<Boolean> original) {
        if (block == Blocks.END_STONE)
            return instance.is(ModTags.Blocks.END_PLANTS_GROWABLE_ON);
        return original.call(instance,block);
    }

    @WrapOperation(method = "updateShape", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    public boolean getStateForNeighborUpdateNihilium(BlockState instance, Block block, Operation<Boolean> original) {
        if (block == Blocks.END_STONE)
            return instance.is(ModTags.Blocks.END_PLANTS_GROWABLE_ON);
        return original.call(instance,block);
    }

    @WrapOperation(method = "getStateWithConnections", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private static boolean withConnectionPropertiesNihilium(BlockState instance, Block block, Operation<Boolean> original) {
        if (block == Blocks.END_STONE)
            return instance.is(ModTags.Blocks.END_PLANTS_GROWABLE_ON);
        return original.call(instance,block);
    }
}