package net.lyof.phantasm.world.biome;

import net.lyof.phantasm.Phantasm;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public class ModBiomes {
    public static HolderGetter<Biome> LOOKUP;

    public static void register(HolderGetter<Biome> lookup) {
        LOOKUP = lookup;
    }

    public static final ResourceKey<Biome> DREAMING_DEN = register("dreaming_den");
    public static final ResourceKey<Biome> ACIDBURNT_ABYSSES = register("acidburnt_abysses");

    public static ResourceKey<Biome> register(String name) {
        return ResourceKey.create(Registries.BIOME, Phantasm.makeID(name));
    }
}
