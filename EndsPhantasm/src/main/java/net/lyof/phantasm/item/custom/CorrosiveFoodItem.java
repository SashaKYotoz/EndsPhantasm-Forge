package net.lyof.phantasm.item.custom;

import net.lyof.phantasm.entity.access.Corrosive;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CorrosiveFoodItem extends DescribedItem {
    public CorrosiveFoodItem(int corrosiveTicks, Item.Properties settings) {
        super(settings);
        this.corrosiveTicks = corrosiveTicks;
    }

    private static final String DESC_KEY = "item.phantasm.pomb_slice.desc";
    private static final String DESC_KEY_1 = "item.phantasm.pop_rock_candy.desc";
    protected int corrosiveTicks;

    @Override
    public String getOrCreateDescTranslationKey() {
        return this.getDefaultInstance().getDescriptionId().contains("pomb") ? DESC_KEY : DESC_KEY_1;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (user instanceof Corrosive corrosive) corrosive.setCorrosiveTicks(this.corrosiveTicks);
        return super.finishUsingItem(stack, world, user);
    }
}
