package net.lyof.phantasm.setup.packets;

import net.lyof.phantasm.block.entities.ChallengeRuneBlockEntity;
import net.lyof.phantasm.entity.access.Challenger;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChallengeEndsPacket {
    private BlockPos pos;
    private boolean success;

    public ChallengeEndsPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.success = buffer.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeBoolean(this.success);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft client = Minecraft.getInstance();
            ChallengeRuneBlockEntity rune = ((Challenger) client.player).getChallengeRune();

            if (rune != null && rune.getBlockPos().equals(pos)) {
                client.gui.setTitle(Component.empty());
                client.gui.setSubtitle(Component.translatable(success ?
                                "block.phantasm.challenge_rune.success" :
                                "block.phantasm.challenge_rune.fail")
                        .withStyle(ChatFormatting.LIGHT_PURPLE));

                rune.stopChallenge(success);
            }

            client.levelRenderer.playStreamingMusic(null, pos);
        });
        context.setPacketHandled(true);
        return true;
    }
}