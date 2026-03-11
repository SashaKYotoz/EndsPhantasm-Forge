package net.lyof.phantasm.mixin;

import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.screen.access.PolyppieInventory;
import net.minecraft.world.Container;
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

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends AbstractContainerMenu implements PolyppieInventory.Handler {
    @Shadow
    @Final
    private Player owner;

    protected InventoryMenuMixin(@Nullable MenuType<?> type, int syncId) {
        super(type, syncId);
    }

    @Unique
    private Container polyppieInventory = null;
    @Unique private Slot phantasm_slot = null;
    @Unique private boolean phantasm_visible = true;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initPolyppieScreenHandler(Inventory inventory, boolean onServer, Player owner, CallbackInfo ci) {
        if (this.owner instanceof PolyppieCarrier carrier) {
            this.polyppieInventory = new PolyppieInventory(carrier);

            int x = 8, y = 166 - 10 + 8;

            this.phantasm_slot = this.addSlot(new Slot(this.polyppieInventory, this.slots.size(), x, y) {
                @Override
                public void onQuickCraft(ItemStack newItem, ItemStack original) {
                    super.onQuickCraft(newItem, original);
                    this.container.setChanged();
                }

                @Override
                public boolean isActive() {
                    return InventoryMenuMixin.this.phantasm_isEnabled()
                            && InventoryMenuMixin.this.phantasm_visible;
                }
            });
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