package net.lyof.phantasm;

import io.netty.buffer.Unpooled;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.setup.ModPackets;
import net.lyof.phantasm.setup.packets.ClientInitializePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ForgeEvents {
    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        event.getPlayerList().getPlayers().forEach(ForgeEvents::syncToPlayer);
    }

    private static void syncToPlayer(ServerPlayer player) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeInt(0);
        List<FriendlyByteBuf> packets = new ArrayList<>();
        packets.add(buffer);
        PolyppieEntity.Variant.write(packets);

        packets.forEach(p -> ModPackets.sendToPlayer(new ClientInitializePacket(p), player));
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            Player newPlayer = event.getEntity();

            if (newPlayer instanceof PolyppieCarrier carrier && carrier.getCarriedPolyppie() != null) {
                carrier.getCarriedPolyppie().stopPlaying();
            }
        }
    }
}