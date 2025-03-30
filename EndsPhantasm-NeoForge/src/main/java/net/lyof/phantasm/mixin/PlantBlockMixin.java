package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.TriState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BushBlock.class, priority = 900)
public class PlantBlockMixin {
    @WrapOperation(method = "mayPlaceOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean customPlantTypes(BlockState instance, TagKey tagKey, Operation<Boolean> original) {
        Block self = (Block) (Object) this;
        return original.call(instance, tagKey) || (self.defaultBlockState().is(ModTags.Blocks.END_PLANTS) && instance.is(ModTags.Blocks.END_PLANTS_GROWABLE_ON));
    }

    @WrapOperation(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canSustainPlant(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/neoforged/neoforge/common/util/TriState;"))
    private TriState canSurvive(BlockState instance, BlockGetter getter, BlockPos pos, Direction direction, BlockState state, Operation<TriState> original) {
        return instance.is(ModTags.Blocks.END_PLANTS_GROWABLE_ON) && state.is(ModTags.Blocks.END_PLANTS) ? TriState.TRUE : original.call(instance, getter, pos, direction, state);
    }
}