package net.lyof.phantasm.screen.access;

import io.netty.buffer.Unpooled;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.setup.ModPackets;
import net.lyof.phantasm.setup.packets.PolyppieClientUpdatePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PolyppieInventory implements Container {
    private final PolyppieCarrier owner;

    public PolyppieInventory(PolyppieCarrier owner) {
        this.owner = owner;
    }

    @Override
    public void setChanged() {
        PolyppieEntity polyppie = this.owner.getCarriedPolyppie();
        if (polyppie != null)
            polyppie.stopPlaying();
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.getItem(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        if (this.owner.getCarriedPolyppie() == null) return ItemStack.EMPTY;
        return this.owner.getCarriedPolyppie().getStack();
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return this.removeItemNoUpdate(slot);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = this.getItem(slot);
        if (!this.isEmpty())
            this.owner.getCarriedPolyppie().setStack(ItemStack.EMPTY);
        this.setChanged();
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        PolyppieEntity polyppie = this.owner.getCarriedPolyppie();
        if (polyppie != null)
            polyppie.setStack(stack);
        this.setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean stillValid(Player player) {
        return player instanceof PolyppieCarrier;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        PolyppieEntity polyppie = this.owner.getCarriedPolyppie();
        return polyppie != null && polyppie.isValidDisc(stack);
    }

    @Override
    public void clearContent() {
        this.removeItemNoUpdate(0);
    }

    public interface Handler {
        int x = -32, y = 100;

        void phantasm_toggleVisibility();
        boolean phantasm_isVisible();

        boolean phantasm_isEnabled();

        int phantasm_getSlotX();
        int phantasm_getSlotY();

        static void onButtonClick(Player player, int id) {
            if (player.level().isClientSide()) {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                buf.writeInt(id);
                ModPackets.sendToServer(new PolyppieClientUpdatePacket(buf));
            }

            if (player instanceof PolyppieCarrier carrier) {
                switch (id) {
                    case 0 -> carrier.getCarriedPolyppie().togglePaused();
                    case 1 -> {
                        carrier.getCarriedPolyppie().stopPlaying();
                        carrier.getCarriedPolyppie().setPaused(false);
                    }
                }
            }
        }
    }
}