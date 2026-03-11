package net.lyof.phantasm.setup.packets;

import io.netty.buffer.Unpooled;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.setup.ReloadListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientInitializePacket {
    private FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());

    public ClientInitializePacket(FriendlyByteBuf buffer) {
        this.buffer = buffer;
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBytes(this.buffer);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            int eventType = buffer.readInt();
            if (eventType == 0)
                ReloadListener.INSTANCE.reloadClient();
            else if (eventType == 1)
                PolyppieEntity.Variant.read(buffer);
        });
        context.setPacketHandled(true);
        return true;
    }
}