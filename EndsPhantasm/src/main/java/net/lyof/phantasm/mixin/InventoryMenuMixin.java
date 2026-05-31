package net.lyof.phantasm.mixin;

import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.screen.access.PolyppieInventory;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends AbstractContainerMenu implements PolyppieInventory.Handler {
    @Shadow
    @Final
    private Player owner;

    protected InventoryMenuMixin(@Nullable MenuType<?> type, int syncId) {
        super(type, syncId);
    }
    @Unique private Slot phantasm_slot = null;
    @Unique private boolean phantasm_visible = true;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initPolyppieScreenHandler(Inventory inventory, boolean onServer, Player owner, CallbackInfo ci) {
        if (this.owner instanceof PolyppieCarrier carrier) {
            int x = 8, y = 166 - 10 + 8;

            this.phantasm_slot = this.addSlot(new Slot(new PolyppieInventory(carrier), this.slots.size(), x, y) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return this.container.canPlaceItem(this.index, stack);
                }

                @Override
                public boolean isActive() {
                    return InventoryMenuMixin.this.phantasm_isEnabled()
                            && InventoryMenuMixin.this.phantasm_visible;
                }
            });
        }
    }
    @Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
    public void quickPolyppieMove(Player player, int slotid, CallbackInfoReturnable<ItemStack> cir) {
        Slot slot = this.getSlot(slotid);
        ItemStack stack = slot.getItem();

        if (slot == this.phantasm_slot && this.moveItemStackTo(stack, 6, 42, true)) {
            cir.setReturnValue(ItemStack.EMPTY);
            this.phantasm_slot.setChanged();
        } else if (this.phantasm_slot.safeInsert(stack).isEmpty()) {
            cir.setReturnValue(ItemStack.EMPTY);
            this.phantasm_slot.setChanged();
        }
    }

    @Override
    public void phantasm_toggleVisibility() {
        this.phantasm_visible = !this.phantasm_visible;
    }

    @Override
    public boolean phantasm_isVisible() {
        return this.phantasm_visible && this.phantasm_isEnabled();
    }

    @Override
    public boolean phantasm_isEnabled() {
        return InventoryMenuMixin.this.owner instanceof PolyppieCarrier carrier
                && carrier.getCarriedPolyppie() != null;
    }

    @Override
    public int phantasm_getSlotX() {
        return this.phantasm_slot.x;
    }

    @Override
    public int phantasm_getSlotY() {
        return this.phantasm_slot.y;
    }
}