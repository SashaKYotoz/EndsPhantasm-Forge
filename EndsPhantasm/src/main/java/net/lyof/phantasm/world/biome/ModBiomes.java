package net.lyof.phantasm.world.biome;

import net.lyof.phantasm.Phantasm;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Musics;
import net.minecraft.world.level.biome.*;

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

    public static void boostrap(BootstapContext<Biome> context) {
        context.register(DREAMING_DEN, dreamingDen(context));
        context.register(ACIDBURNT_ABYSSES, acidburntAbysses(context));
    }

    public static Biome dreamingDen(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0.5f)
                .temperature(0.5f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .skyColor(0x30c918)
                        .fogColor(0x22a1e6)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(Musics.END).build()
                ).build();
    }

    public static Biome acidburntAbysses(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0.5f)
                .temperature(0.5f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .skyColor(0x30c918)
                        .fogColor(0x22a1e6)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(Musics.END).build()
                ).build();
    }
}
