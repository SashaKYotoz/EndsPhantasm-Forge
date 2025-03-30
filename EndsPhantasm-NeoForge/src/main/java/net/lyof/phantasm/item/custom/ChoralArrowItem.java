package net.lyof.phantasm.item.custom;

import net.lyof.phantasm.entities.custom.ChoralArrowEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChoralArrowItem extends ArrowItem {
    public ChoralArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        return ChoralArrowEntity.create(level,shooter);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.phantasm.choral_arrow.desc.bow").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("item.phantasm.choral_arrow.desc.crossbow").withStyle(ChatFormatting.GRAY));
    }
}