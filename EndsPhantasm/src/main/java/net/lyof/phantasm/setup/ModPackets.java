package net.lyof.phantasm.setup;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.setup.packets.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPackets {
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(Phantasm.makeID("messages"))
            .networkProtocolVersion(() -> "1.0")
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();
    public static int PACKET_ID = 0;

    public static void registerPackets(FMLCommonSetupEvent ignored) {
        registerC2SPackets();
        registerS2CPackets();
    }

    private static void registerC2SPackets() {
        INSTANCE.messageBuilder(BeginCutsceneEndsPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(BeginCutsceneEndsPacket::new)
                .encoder(BeginCutsceneEndsPacket::toBytes)
                .consumerMainThread(BeginCutsceneEndsPacket::handle)
                .add();
        INSTANCE.messageBuilder(PolyppieClientUpdatePacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(PolyppieClientUpdatePacket::new)
                .encoder(PolyppieClientUpdatePacket::toBytes)
                .consumerMainThread(PolyppieClientUpdatePacket::handle)
                .add();
    }

    private static void registerS2CPackets() {
        INSTANCE.messageBuilder(ClientInitializePacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientInitializePacket::new)
                .encoder(ClientInitializePacket::toBytes)
                .consumerMainThread(ClientInitializePacket::handle)
                .add();
        INSTANCE.messageBuilder(BeginCutsceneStartsPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(BeginCutsceneStartsPacket::new)
                .encoder(BeginCutsceneStartsPacket::toBytes)
                .consumerMainThread(BeginCutsceneStartsPacket::handle)
                .add();
        INSTANCE.messageBuilder(BehemothAwakesPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(BehemothAwakesPacket::new)
                .encoder(BehemothAwakesPacket::toBytes)
                .consumerMainThread(BehemothAwakesPacket::handle)
                .add();
        INSTANCE.messageBuilder(PolyppieServerUpdatePacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PolyppieServerUpdatePacket::new)
                .encoder(PolyppieServerUpdatePacket::toBytes)
                .consumerMainThread(PolyppieServerUpdatePacket::handle)
                .add();
        INSTANCE.messageBuilder(PolyppieStartsBeingCarriedPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PolyppieStartsBeingCarriedPacket::new)
                .encoder(PolyppieStartsBeingCarriedPacket::toBytes)
                .consumerMainThread(PolyppieStartsBeingCarriedPacket::handle)
                .add();
        INSTANCE.messageBuilder(PolyppieStopsBeingCarriedPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PolyppieStopsBeingCarriedPacket::new)
                .encoder(PolyppieStopsBeingCarriedPacket::toBytes)
                .consumerMainThread(PolyppieStopsBeingCarriedPacket::handle)
                .add();
        INSTANCE.messageBuilder(ChallengeStartsPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ChallengeStartsPacket::new)
                .encoder(ChallengeStartsPacket::toBytes)
                .consumerMainThread(ChallengeStartsPacket::handle)
                .add();
        INSTANCE.messageBuilder(ChallengeEndsPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ChallengeEndsPacket::new)
                .encoder(ChallengeEndsPacket::toBytes)
                .consumerMainThread(ChallengeEndsPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToPlayersNearby(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), message);
    }

    public static <MSG> void sendToPlayersNearbyAndSelf(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), message);
    }

    public static <MSG> void sendToAll(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}