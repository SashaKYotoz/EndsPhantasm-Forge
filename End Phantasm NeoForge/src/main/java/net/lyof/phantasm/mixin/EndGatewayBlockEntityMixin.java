package net.lyof.phantasm.mixin;

import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TheEndGatewayBlockEntity.class)
public class EndGatewayBlockEntityMixin {
    @Redirect(method = "findTallestBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private static boolean invalidStar(BlockState instance, Block block) {
        return instance.is(block) || instance.is(ModBlocks.FALLEN_STAR.get());
    }
}