package net.lyof.phantasm.setup.packets;

import net.lyof.phantasm.block.entities.ChallengeRuneBlockEntity;
import net.lyof.phantasm.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChallengeStartsPacket {
    private BlockPos pos;

    public ChallengeStartsPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handle(pos)));
        context.setPacketHandled(true);
        return true;
    }

    static class ClientPacketHandler {
        public static void handle(BlockPos pos) {
            Minecraft client = Minecraft.getInstance();
            if (client.level.getBlockEntity(pos) instanceof ChallengeRuneBlockEntity rune) {
                rune.startChallenge();
                rune.addChallenger(client.player);
                client.levelRenderer.playStreamingMusic(ModSounds.CHALLENGE, pos);
                client.gui.setTitle(Component.empty());
                client.gui.setSubtitle(Component.translatable("block.phantasm.challenge_rune.start")
                        .withStyle(ChatFormatting.LIGHT_PURPLE));
            }
        }
    }
}