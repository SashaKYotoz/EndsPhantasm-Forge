package net.lyof.phantasm.world.biome;

import com.mojang.datafixers.util.Pair;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EndDataCompat {
    public static void register() {
        add(ModBiomes.DREAMING_DEN, () -> ConfigEntries.doDreamingDenBiome);
        add(Biomes.END_HIGHLANDS, () -> ConfigEntries.doDreamingDenBiome && ConfigEntries.doAcidburntAbyssesBiome
                && getCompatibilityMode().equals("endercon"));
        add(ModBiomes.ACIDBURNT_ABYSSES, () -> ConfigEntries.doAcidburntAbyssesBiome);
    }

    public static String getCompatibilityMode() {
        boolean auto = ConfigEntries.dataCompatMode.equals("automatic");
        if (auto) {
            if (ModList.get().isLoaded("mr_endercon"))
                return "endercon";
            else if (ModList.get().isLoaded("nullscape"))
                return "nullscape";
            else return "default";
        }
        return ConfigEntries.dataCompatMode;
    }


    private static final List<Pair<ResourceKey<Biome>, Supplier<Boolean>>> BIOMES = new ArrayList<>();

    public static void add(ResourceKey<Biome> biome, Supplier<Boolean> condition) {
        BIOMES.add(new Pair<>(biome, condition));
    }

    public static boolean contains(Holder<Biome> biome) {
        return BIOMES.stream().anyMatch(pair -> pair.getFirst() == biome);
    }

    public static List<ResourceKey<Biome>> getEnabledBiomes() {
        List<ResourceKey<Biome>> result = new ArrayList<>();
        for (Pair<ResourceKey<Biome>, Supplier<Boolean>> pair : BIOMES) {
            if (pair.getSecond().get())
                result.add(pair.getFirst());
        }
        return result;
    }
}
