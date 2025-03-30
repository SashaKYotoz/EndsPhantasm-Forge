package net.lyof.phantasm.world;

import net.lyof.phantasm.Phantasm;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.*;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> PREAM = create("pream");
    public static final ResourceKey<PlacedFeature> TALL_PREAM = create("tall_pream");
    public static final ResourceKey<PlacedFeature> CRYSTAL_SPIKE = create("crystal_spike");
    public static final ResourceKey<PlacedFeature> FALLEN_STAR = create("fallen_star");
    public static final ResourceKey<PlacedFeature> VIVID_NIHILIUM_PATCH = create("patch_vivid_nihilis");
    public static final ResourceKey<PlacedFeature> TALL_VIVID_NIHILIUM_PATCH = create("patch_tall_vivid_nihilis");
    public static final ResourceKey<PlacedFeature> OBLIVINE_PATCH = create("patch_oblivine");
    public static final ResourceKey<PlacedFeature> STARFLOWER_PATCH = create("patch_starflower");
    public static ResourceKey<PlacedFeature> create(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE,ResourceLocation.fromNamespaceAndPath(Phantasm.MOD_ID,name));
    }
}
