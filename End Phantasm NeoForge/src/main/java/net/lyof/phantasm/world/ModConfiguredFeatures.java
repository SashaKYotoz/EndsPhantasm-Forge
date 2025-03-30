package net.lyof.phantasm.world;

import net.lyof.phantasm.Phantasm;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PREAM = create("pream");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_PREAM = create("tall_pream");

    public static final ResourceKey<ConfiguredFeature<?, ?>> CRYSTAL_SPIKE = create("crystal_spike");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_STAR = create("fallen_star");
    public static final ResourceKey<ConfiguredFeature<?, ?>> STARFLOWER = create("patch_starflower");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VIVID_NIHILIUM = create("patch_vivid_nihilis");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_VIVID_NIHILIUM = create("patch_tall_vivid_nihilis");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OBSIDIAN_TOWER = create("obsidian_tower");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OBLIVINE = create("patch_oblivine");

    public static ResourceKey<ConfiguredFeature<?, ?>> create(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Phantasm.MOD_ID, name));
    }
}
