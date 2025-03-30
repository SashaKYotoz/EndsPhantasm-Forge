package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ChorusFlowerBlock.class, priority = 972)
public class ChorusFlowerBlockMixin {
    @WrapOperation(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    public boolean canPlaceAtNihilium(BlockState instance, Block block, Operation<Boolean> original) {
        if (ModList.get().isLoaded("endergetic"))
            return original.call(instance, block)
                    || instance.is(ModTags.Blocks.END_PLANTS_GROWABLE_ON)
                    || instance.is(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("endergetic:poismoss")));
        return original.call(instance, block) || instance.is(ModTags.Blocks.END_PLANTS_GROWABLE_ON);
    }
}