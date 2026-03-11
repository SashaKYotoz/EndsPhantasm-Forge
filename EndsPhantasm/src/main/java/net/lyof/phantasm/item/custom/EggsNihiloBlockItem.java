package net.lyof.phantasm.item.custom;

import net.lyof.phantasm.Phantasm;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class EggsNihiloBlockItem extends BlockItem {
    public EggsNihiloBlockItem(Block block, Item.Properties settings) {
        super(block, settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        return InteractionResultHolder.pass(user.getItemInHand(hand));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return this.place(new BlockPlaceContext(context));
    }

    protected String descKey;

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);

        if (Phantasm.isFarmersDelightLoaded())
            TextUtils.addFoodEffectTooltip(stack, tooltip, 1);

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
