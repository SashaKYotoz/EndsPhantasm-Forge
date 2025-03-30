package net.lyof.phantasm.world;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.feature.*;
import net.lyof.phantasm.world.feature.config.BoulderFeatureConfig;
import net.lyof.phantasm.world.feature.config.CrystalSpikeFeatureConfig;
import net.lyof.phantasm.world.feature.config.DralgaeFeatureConfig;
import net.lyof.phantasm.world.feature.config.SingleBlockFeatureConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(Registries.FEATURE, Phantasm.MOD_ID);
    public static final DeferredHolder<Feature<?>, Feature<CrystalSpikeFeatureConfig>> CRYSTAL_SPIKE = REGISTRY.register("crystal_spike",()-> CrystalSpikeFeature.INSTANCE);
    public static final DeferredHolder<Feature<?>, Feature<SingleBlockFeatureConfig>> SINGLE_BLOCK = REGISTRY.register("single_block",()-> SingleBlockFeature.INSTANCE);
    public static final DeferredHolder<Feature<?>, Feature<CountConfiguration>> OBSIDIAN_TOWER = REGISTRY.register("obsidian_tower",()-> ObsidianTowerStructure.INSTANCE);
    public static final DeferredHolder<Feature<?>, Feature<BlockColumnConfiguration>> OBLIVINE = REGISTRY.register("oblivine",()-> OblivineFeature.INSTANCE);
    public static final DeferredHolder<Feature<?>, Feature<DralgaeFeatureConfig>> DRALGAE = REGISTRY.register("dralgae",()-> DralgaeFeature.INSTANCE);
    public static final DeferredHolder<Feature<?>, Feature<DralgaeFeatureConfig>> HUGE_DRALGAE = REGISTRY.register("huge_dralgae",()-> HugeDralgaeFeature.INSTANCE);
    public static final DeferredHolder<Feature<?>, Feature<BoulderFeatureConfig>> BOULDER = REGISTRY.register("boulder",()-> BoulderFeature.INSTANCE);
    public static final DeferredHolder<Feature<?>, Feature<BoulderFeatureConfig>> CEILING_BOULDER = REGISTRY.register("ceiling_boulder",()-> CeilingBoulderFeature.INSTANCE);
    public static final DeferredHolder<Feature<?>, Feature<BoulderFeatureConfig>> CEILING_SPIKE = REGISTRY.register("ceiling_spike",()-> CeilingSpikeFeature.INSTANCE);
}
