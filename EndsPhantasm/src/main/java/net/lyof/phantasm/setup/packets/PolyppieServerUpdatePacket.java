package net.lyof.phantasm.setup.packets;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.sound.SongHandler;
import net.lyof.phantasm.sound.custom.PolyppieDiscSoundInstance;
import net.lyof.phantasm.sound.custom.PolyppieSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PolyppieServerUpdatePacket {
    private int id = 0;
    private int soundKey = 0;
    private CompoundTag nbt = new CompoundTag();


    public PolyppieServerUpdatePacket(FriendlyByteBuf buffer) {
        this.nbt = buffer.readNbt();
        this.id = buffer.readInt();
        this.soundKey = buffer.readInt();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeNbt(this.nbt);
        buffer.writeInt(this.id);
        buffer.writeInt(this.soundKey);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft client = Minecraft.getInstance();
            Entity self = client.level.getEntity(id);
            ItemStack stack = ItemStack.EMPTY;
            if (!nbt.isEmpty()) stack = ItemStack.of(nbt);

            PolyppieEntity polyppie = null;
            if (self instanceof PolyppieCarrier carrier && carrier.getCarriedPolyppie() != null)
                polyppie = carrier.getCarriedPolyppie();
            else if (self instanceof PolyppieEntity)
                polyppie = (PolyppieEntity) self;

            PolyppieSoundInstance soundInstance = SongHandler.instance.get(soundKey);
            if (soundInstance != null) {
                SongHandler.instance.remove(soundKey);
                client.getSoundManager().stop(soundInstance);
            }
            Phantasm.log(stack.toString(),1);

            if (polyppie != null) {
                polyppie.setSoundKey(soundKey);
                if (stack.isEmpty()) polyppie.stopPlaying();
                else polyppie.startPlaying();
                if (polyppie.isDeadOrDying()) return;
//if (Phantasm.isVinURLLoaded() && VinURLCompat.isVinURLDisc(stack)) {
//                    VinURLCompat.playSound(polyppie, soundKey, stack, client);
//                } else
                if (stack.getItem() instanceof RecordItem musicDisc) {
                    client.gui.setNowPlaying(musicDisc.getDescription());

                    soundInstance = new PolyppieDiscSoundInstance(musicDisc.getSound(), 1, polyppie, 0);
                    SongHandler.instance.add(soundKey, soundInstance);
                    client.getSoundManager().play(soundInstance);
                }
            }
        });
        return true;

    }
}