package net.lyof.phantasm.mixin;

import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    @Shadow
    @Final
    private DataSlot cost;

    @Shadow
    public int repairItemCountCost;

    @Shadow
    @Nullable
    private String itemName;

    public AnvilMenuMixin(@Nullable MenuType<?> type, int syncId, Inventory playerInventory, ContainerLevelAccess context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(method = "createResult", at = @At(value = "HEAD"), cancellable = true)
    public void oblivionRepair(CallbackInfo ci) {
        ItemStack stack = this.resultSlots.getItem(0);
        if (!stack.isEmpty() && stack.getDamageValue() > 0 && this.inputSlots.getItem(1).is(ModBlocks.OBLIVION.get().asItem())) {
            int dura = Math.min((stack.getDamageValue() + 199) / 200, this.inputSlots.getItem(1).getCount());
            ItemStack output = stack.copy();
            output.setDamageValue(output.getDamageValue() - 200 * dura);

            this.repairItemCountCost = dura;

            if (this.itemName != null && !Util.isBlank(this.itemName)) {
                if (!this.itemName.equals(stack.getHoverName().getString())) {
                    dura += 1;
                    output.setHoverName(Component.literal(this.itemName));
                }
            } else if (stack.hasCustomHoverName()) {
                dura += 1;
                output.resetHoverName();
            }

            this.cost.set(dura);

            this.resultSlots.setItem(0, output);
            ci.cancel();
        }
    }
}
