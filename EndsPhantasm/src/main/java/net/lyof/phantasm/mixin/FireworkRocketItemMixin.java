package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireworkRocketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FireworkRocketItem.class)
public class FireworkRocketItemMixin {
    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isFallFlying()Z"))
    public boolean cancelBoost(Player instance, Operation<Boolean> original) {
        if (instance instanceof ServerPlayer player) {
            Advancement freeTheEnd = player.getServer().getAdvancements().getAdvancement(new ResourceLocation(ConfigEntries.elytraBoostAdvancement));
            if ((freeTheEnd == null || player.getAdvancements().getOrStartProgress(freeTheEnd).isDone()) && player.isFallFlying()) {
                instance.swing(instance.getUsedItemHand(), true);
                return original.call(instance);
            } else if (freeTheEnd != null && player.isFallFlying()) {
                player.displayClientMessage(Component.translatable("item.minecraft.firework_rocket.cannot_use", freeTheEnd.getChatComponent()),
                        true);
            }
        }
        return false;
    }
}