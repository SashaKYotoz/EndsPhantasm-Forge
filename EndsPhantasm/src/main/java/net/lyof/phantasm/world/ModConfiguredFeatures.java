package net.lyof.phantasm.world;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.world.feature.CrystalSpikeFeature;
import net.lyof.phantasm.world.feature.OblivineFeature;
import net.lyof.phantasm.world.feature.ObsidianTowerStructure;
import net.lyof.phantasm.world.feature.SingleBlockFeature;
import net.lyof.phantasm.world.feature.config.CrystalSpikeFeatureConfig;
import net.lyof.phantasm.world.feature.config.SingleBlockFeatureConfig;
import net.lyof.phantasm.world.feature.tree.custom.PreamFoliagePlacer;
import net.lyof.phantasm.world.feature.tree.custom.PreamTrunkPlacer;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLeavesDecorator;

import java.util.List;

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
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Phantasm.makeID( name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        register(context, PREAM, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.PREAM_LOG.get()),
                new PreamTrunkPlacer(3, 2, 6),
                BlockStateProvider.simple(ModBlocks.PREAM_LEAVES.get()),
                new PreamFoliagePlacer(UniformInt.of(3, 5), ConstantInt.of(0)),
                new TwoLayersFeatureSize(1, 0, 2)
        ).dirt(BlockStateProvider.simple(Blocks.END_STONE))
                .decorators(List.of(
                        new AttachedToLeavesDecorator(
                                0.2f,
                                1,
                                0,
                                SimpleStateProvider.simple(ModBlocks.HANGING_PREAM_LEAVES.get().defaultBlockState()),
                                1,
                                List.of(Direction.DOWN)
                        )
                )).build());

        register(context, TALL_PREAM, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.PREAM_LOG.get()),
                new PreamTrunkPlacer(2, 2, 6),
                BlockStateProvider.simple(ModBlocks.PREAM_LEAVES.get().defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)),
                new PreamFoliagePlacer(UniformInt.of(5, 6), ConstantInt.of(0)),
                new TwoLayersFeatureSize(1, 0, 2)
        ).dirt(BlockStateProvider.simple(Blocks.END_STONE))
                .decorators(List.of(
                                new AttachedToLeavesDecorator(
                                        0.2f,
                                        1,
                                        0,
                                        SimpleStateProvider.simple(ModBlocks.HANGING_PREAM_LEAVES.get().defaultBlockState()),
                                        1,
                                        List.of(Direction.DOWN)
                                ),
                                new AttachedToLeavesDecorator(
                                        0.1f,
                                        1,
                                        0,
                                        SimpleStateProvider.simple(ModBlocks.PREAM_LOG.get().defaultBlockState()),
                                        1,
                                        List.of(Direction.DOWN)
                                )
                        )
                ).build());
        register(context, CRYSTAL_SPIKE, CrystalSpikeFeature.INSTANCE,
                new CrystalSpikeFeatureConfig(UniformInt.of(3, 5), 0.3f));

        register(context, FALLEN_STAR, SingleBlockFeature.INSTANCE,
                new SingleBlockFeatureConfig(UniformInt.of(120, 180), BlockStateProvider.simple(ModBlocks.FALLEN_STAR.get())));
        register(context, STARFLOWER, Feature.FLOWER, new RandomPatchConfiguration(
                16, 8, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.STARFLOWER.get())))));
        register(context, VIVID_NIHILIUM, Feature.FLOWER, new RandomPatchConfiguration(
                48, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.VIVID_NIHILIS.get())))));
        register(context, VIVID_NIHILIUM, Feature.FLOWER, new RandomPatchConfiguration(
                20, 10, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.VIVID_NIHILIS.get())))));
        register(context, OBSIDIAN_TOWER, ObsidianTowerStructure.INSTANCE, new CountConfiguration(UniformInt.of(30, 50)));
        register(context, OBLIVINE, OblivineFeature.INSTANCE,
                BlockColumnConfiguration.simple(UniformInt.of(5, 8), BlockStateProvider.simple(ModBlocks.OBLIVINE.get())));
        //register(context, RAW_PURPUR_CABIN, PurpurCabinFeature.INSTANCE, new PurpurCabinFeatureConfig(
        //        UniformIntProvider.create(6, 9), UniformIntProvider.create(6, 9)));
    }

    //public static final RegistryKey<ConfiguredFeature<?, ?>> RAW_PURPUR_CABIN = create("raw_purpur_maze");
}
