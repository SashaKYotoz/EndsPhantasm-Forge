package net.lyof.phantasm.setup.packets;

import net.lyof.phantasm.setup.ModPackets;
import net.lyof.phantasm.util.MixinAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BeginCutsceneStartsPacket {
    public BeginCutsceneStartsPacket() {}

    public BeginCutsceneStartsPacket(FriendlyByteBuf buffer) {}

    public void toBytes(FriendlyByteBuf buffer) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler::handleBeginCutscene));

        context.setPacketHandled(true);
        return true;
    }
    static class ClientPacketHandler {
        public static void handleBeginCutscene() {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level == null) return;
            if (minecraft.screen instanceof WinScreen) return;

            WinScreen creditsScreen = new WinScreen(true, () -> {
                ModPackets.sendToServer(new BeginCutsceneEndsPacket());
                minecraft.setScreen(null);
            });

            ((MixinAccess<Boolean>) creditsScreen).setMixinValue(true);
            minecraft.setScreen(creditsScreen);
        }
    }
}