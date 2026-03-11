package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.entities.ChallengeRuneBlockEntity;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EndCrystalItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EndCrystalItem.class)
public class EndCrystalItemMixin {
    @WrapOperation(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    public boolean isObsidian(BlockState instance, Block block, Operation<Boolean> original, UseOnContext context) {
        if (block == Blocks.OBSIDIAN) {
            if (original.call(instance, block) || instance.is(ModTags.Blocks.END_CRYSTAL_PLACEABLE_ON))
                return true;
            if (instance.is(ModBlocks.CHALLENGE_RUNE.get()))
                    return context.getPlayer() instanceof ServerPlayer serverPlayer
                            && context.getLevel().getBlockEntity(context.getClickedPos()) instanceof ChallengeRuneBlockEntity rune
                            && rune.canStart(serverPlayer);
        }
        return original.call(instance, block);
    }

    @WrapMethod(method = "useOn")
    public InteractionResult startChallenge(UseOnContext context, Operation<InteractionResult> original) {
        InteractionResult result = original.call(context);

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player user = context.getPlayer();

        if (!(user instanceof ServerPlayer serverPlayer)) return result;

        if (world.getBlockEntity(pos) instanceof ChallengeRuneBlockEntity rune) {
            if (result.consumesAction())
                rune.startChallenge(user);
            else
                rune.displayHint(rune.getStartingCondition(serverPlayer), serverPlayer);
            user.swing(context.getHand(), true);
        }
        return result;
    }
}
