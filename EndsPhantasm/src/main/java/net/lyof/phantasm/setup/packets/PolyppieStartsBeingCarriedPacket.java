package net.lyof.phantasm.setup.packets;

import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PolyppieStartsBeingCarriedPacket {
    private int polyppieid = 0;
    private int playerid = 0;

    public PolyppieStartsBeingCarriedPacket(FriendlyByteBuf buffer) {
        this.polyppieid = buffer.readInt();
        this.playerid = buffer.readInt();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(this.polyppieid);
        buffer.writeInt(this.playerid);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft client = Minecraft.getInstance();
            if (client.level.getEntity(polyppieid) instanceof PolyppieEntity polyppie
                    && client.level.getEntity(playerid) instanceof Player player) {
                polyppie.setCarriedBy(player, null);
            }
        });
        return true;

    }
}