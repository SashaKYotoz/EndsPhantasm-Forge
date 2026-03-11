package net.lyof.phantasm.setup.packets;

import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BehemothAwakesPacket {
    private int selfId = 0;
    private int targetId = 0;

    public BehemothAwakesPacket(FriendlyByteBuf buffer) {
        this.selfId = buffer.readInt();
        this.targetId = buffer.readInt();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(this.selfId);
        buffer.writeInt(this.targetId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            Entity self = minecraft.level.getEntity(selfId);
            Entity target = minecraft.level.getEntity(targetId);
            if (self instanceof BehemothEntity behemoth)
                behemoth.setTarget((LivingEntity) target);
        });
        return true;

    }
}