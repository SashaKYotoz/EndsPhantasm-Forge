package net.lyof.phantasm.setup.packets;

import net.lyof.phantasm.util.MixinAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BeginCutsceneEndsPacket {
    public BeginCutsceneEndsPacket() {}

    public BeginCutsceneEndsPacket(FriendlyByteBuf buffer) {}

    public void toBytes(FriendlyByteBuf buffer) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.hasChangedDimension();
                ((MixinAccess<Boolean>) player).setMixinValue(true);
            }
        });
        context.setPacketHandled(true);
        return true;
    }
}