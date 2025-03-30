package net.lyof.phantasm.mixin;

import net.lyof.phantasm.setup.ModTags;
import net.minecraft.ChatFormatting;
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
        if (stack.is(ModTags.Items.XP_BOOSTED))
            components.add(Component.translatable("tooltip.xp_boosted").withStyle(ChatFormatting.DARK_GREEN));
    }
}