package net.lyof.phantasm.mixin;

import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Inject(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
    private void putPolyppieDown(ServerPlayer player, Level world, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {

        if (player instanceof PolyppieCarrier carrier && carrier.getCarriedPolyppie() != null && stack.isEmpty()
                && hitResult.getDirection() == Direction.UP && !hitResult.isInside() && player.isCrouching()) {

            BlockPos pos = hitResult.getBlockPos().above();

            AABB boundingBox = ModEntities.POLYPPIE.get().getAABB(hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z);
            List<AABB> occupiedSpace = new ArrayList<>();
            for (BlockPos p : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 1, 1))) {
                if (!world.getBlockState(p).getCollisionShape(world, p).isEmpty())
                    occupiedSpace.add(new AABB(p));
            }

            for (AABB box : occupiedSpace) {
                if (boundingBox.intersects(box))
                    return;
            }

            carrier.getCarriedPolyppie().setCarriedBy(player, hitResult.getLocation());
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}