package net.lyof.phantasm.world;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.ArrayList;
import java.util.List;

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
        return ResourceKey.create(Registries.PLACED_FEATURE,Phantasm.makeID(name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        var configLookup = context.lookup(Registries.CONFIGURED_FEATURE);

        List<PlacementModifier> modifiers = new ArrayList<>();
        //modifiers.add(RarityFilterPlacementModifier.of(2));
        modifiers.addAll(VegetationPlacements.treePlacement(
                PlacementUtils.countExtra(1, 0.5f, 2),
                ModBlocks.PREAM_SAPLING.get()));


        register(context, PREAM, configLookup.getOrThrow(ModConfiguredFeatures.PREAM),
                modifiers);
        modifiers.add(RarityFilter.onAverageOnceEvery(6));
        register(context, TALL_PREAM, configLookup.getOrThrow(ModConfiguredFeatures.TALL_PREAM),
                modifiers);

        register(context, CRYSTAL_SPIKE, configLookup.getOrThrow(ModConfiguredFeatures.CRYSTAL_SPIKE),List.of(InSquarePlacement.spread(), RarityFilter.onAverageOnceEvery(2)));

        register(context, FALLEN_STAR, configLookup.getOrThrow(ModConfiguredFeatures.FALLEN_STAR),List.of(
                InSquarePlacement.spread(),
                RarityFilter.onAverageOnceEvery(3)));

        register(context, VIVID_NIHILIUM_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.VIVID_NIHILIUM),List.of(CountPlacement.of(6),InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));
        register(context, TALL_VIVID_NIHILIUM_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.VIVID_NIHILIUM),List.of(CountPlacement.of(4),InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));
        register(context, STARFLOWER_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.STARFLOWER),
                List.of(InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE));
        register(context, OBLIVINE_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.OBLIVINE),
                List.of(InSquarePlacement.spread(),
                        PlacementUtils.countExtra(10, 1, 3),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE));
        //register(context, RAW_PURPUR_CABIN, configLookup.getOrThrow(ModConfiguredFeatures.RAW_PURPUR_CABIN),
        //        SquarePlacementModifier.of(),
        //        PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
        //        RarityFilterPlacementModifier.of(10)
        //        );
    }

    //public static final RegistryKey<PlacedFeature> RAW_PURPUR_CABIN = create("raw_purpur_maze");
}
