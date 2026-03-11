package net.lyof.phantasm.setup.packets;

import net.lyof.phantasm.screen.access.PolyppieInventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PolyppieClientUpdatePacket {
    private int id = 1;

    public PolyppieClientUpdatePacket(FriendlyByteBuf buffer) {
        this.id = buffer.readInt();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(this.id);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        if (context.getSender() != null)
            context.enqueueWork(() -> PolyppieInventory.Handler.onButtonClick(context.getSender(), id));
        return true;

    }
}