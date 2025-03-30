package net.lyof.phantasm.mixin;

import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireworkRocketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FireworkRocketItem.class, priority = 900)
public class FireworkRocketItemMixin {
    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isFallFlying()Z"))
    public boolean cancelBoost(Player instance) {
        if (instance instanceof ServerPlayer player) {
            AdvancementHolder freeTheEnd = player.getServer().getAdvancements().get(ResourceLocation.parse(ConfigEntries.elytraBoostAdvancement));
            if ((freeTheEnd == null || player.getAdvancements().getOrStartProgress(freeTheEnd).isDone()) && player.isFallFlying()) {
                instance.swing(instance.getUsedItemHand(), true);
                return true;
            } else if (freeTheEnd != null && player.isFallFlying() && freeTheEnd.value().name().isPresent())
                player.displayClientMessage(Component.translatable("item.minecraft.firework_rocket.cannot_use", freeTheEnd.value().name().get()),
                        true);
        }
        return false;
    }
}