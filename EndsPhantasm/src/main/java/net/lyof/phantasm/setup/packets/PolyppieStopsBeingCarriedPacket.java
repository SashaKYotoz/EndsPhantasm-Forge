package net.lyof.phantasm.setup.packets;

import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.sound.SongHandler;
import net.lyof.phantasm.sound.custom.PolyppieSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PolyppieStopsBeingCarriedPacket {
    private int polyppieid = 0;
    private int playerid = 0;
    double x, y, z;

    public PolyppieStopsBeingCarriedPacket(FriendlyByteBuf buffer) {
        this.polyppieid = buffer.readInt();
        this.playerid = buffer.readInt();
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(this.polyppieid);
        buffer.writeInt(this.playerid);
        buffer.writeDouble(this.x);
        buffer.writeDouble(this.y);
        buffer.writeDouble(this.z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft client = Minecraft.getInstance();
            if (client.level.getEntity(polyppieid) instanceof PolyppieEntity polyppie
                    && client.level.getEntity(playerid) instanceof Player player) {
                PolyppieSoundInstance soundInstance = SongHandler.instance.get(polyppie.getSoundKey());
                if (soundInstance != null) soundInstance.update(polyppie);

                polyppie.setCarriedBy(player, new Vec3(x, y, z));
            }
        });
        return true;

    }
}