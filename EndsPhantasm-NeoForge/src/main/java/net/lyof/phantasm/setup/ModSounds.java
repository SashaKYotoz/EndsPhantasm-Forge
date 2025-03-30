package net.lyof.phantasm.setup;

import net.lyof.phantasm.Phantasm;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final ResourceKey<JukeboxSong> ABRUPTION = createSong("abruption");

    private static ResourceKey<JukeboxSong> createSong(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, Phantasm.makeID(name));
    }
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Phantasm.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> BEHEMOTH_AMBIENT = SOUND_EVENTS.register(
            "entity.behemoth_ambient",
            () -> SoundEvent.createVariableRangeEvent(Phantasm.makeID("entity.behemoth_ambient"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> BEHEMOTH_DYING = SOUND_EVENTS.register(
            "entity.behemoth_dying",
            () -> SoundEvent.createVariableRangeEvent(Phantasm.makeID("entity.behemoth_dying"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_DISC_ABRUPTION = SOUND_EVENTS.register(
            "music_disc_abruption",
            () -> SoundEvent.createVariableRangeEvent(Phantasm.makeID("music_disc_abruption"))
    );
}