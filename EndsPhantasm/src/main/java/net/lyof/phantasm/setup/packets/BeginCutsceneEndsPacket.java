package net.lyof.phantasm.setup.packets;

import net.lyof.phantasm.util.MixinAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
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
                ((MixinAccess<Boolean>) player).setMixinValue(true);

                ServerLevel endLevel = player.server.getLevel(Level.END);
                if (endLevel != null) {
                    player.teleportTo(endLevel, player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
                }
            }
        });
        context.setPacketHandled(true);
        return true;
    }
}