package net.lyof.phantasm.mixin.client;

import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(at = @At("HEAD"), method = "appendHoverText")
    public void showCrystalBonus(ItemStack stack, Level level, List<Component> components, TooltipFlag flag, CallbackInfo ci) {
        if (stack.is(ModTags.Items.XP_BOOSTED) && Minecraft.getInstance().player != null) {
            components.add(Component.translatable("tooltip.xp_boosted").withStyle(ChatFormatting.GREEN));
            String bonus = Math.round((float) ConfigEntries.crystallineXpBoost * Minecraft.getInstance().player.experienceLevel) + "%";
            components.add(Component.translatable("tooltip.xp_boosted.value", bonus).withStyle(ChatFormatting.GREEN));
        }
    }
}