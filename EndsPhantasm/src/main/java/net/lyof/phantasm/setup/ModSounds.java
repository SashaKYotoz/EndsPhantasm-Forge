package net.lyof.phantasm.setup;

import net.lyof.phantasm.Phantasm;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {
    public static void register() {
        Phantasm.log("Registering Sounds for mod id : " + Phantasm.MOD_ID);
    }

    private static SoundEvent create(String name) {
        ResourceLocation location = Phantasm.makeID(name);
        SoundEvent sound = SoundEvent.createVariableRangeEvent(location);
        ForgeRegistries.SOUND_EVENTS.register(location, sound);
        return sound;
    }


    public static final SoundEvent MUSIC_DISC_ABRUPTION = create("music_disc_abruption");

    public static final SoundEvent BEHEMOTH_AMBIENT = create("entity.behemoth_ambient");
    public static final SoundEvent BEHEMOTH_DYING = create("entity.behemoth_dying");
}