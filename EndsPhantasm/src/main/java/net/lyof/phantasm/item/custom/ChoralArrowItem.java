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

import java.util.List;

public class ChoralArrowItem extends ArrowItem {
    public ChoralArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity entity) {
        return ChoralArrowEntity.create(level,entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        components.add(Component.translatable("item.phantasm.choral_arrow.desc.bow").withStyle(ChatFormatting.GRAY));
        components.add(Component.translatable("item.phantasm.choral_arrow.desc.crossbow").withStyle(ChatFormatting.GRAY));
    }
}