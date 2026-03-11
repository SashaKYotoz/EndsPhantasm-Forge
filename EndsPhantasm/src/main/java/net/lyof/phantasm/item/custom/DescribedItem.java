package net.lyof.phantasm.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DescribedItem extends Item {
    public DescribedItem(Item.Properties settings) {
        super(settings);
    }

    protected String descKey;

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);

        String[] txt = Component.translatable(this.getOrCreateDescTranslationKey()).getString().split("\\n");
        for (String t : txt)
            tooltip.add(Component.literal(t).withStyle(ChatFormatting.GRAY));
    }

    public String getOrCreateDescTranslationKey() {
        if (this.descKey == null)
            this.descKey = this.getOrCreateDescriptionId() + ".desc";
        return this.descKey;
    }
}
