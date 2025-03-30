package net.lyof.phantasm.world;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.feature.*;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.FEATURES, Phantasm.MOD_ID);
    public static final RegistryObject<Feature<?>> CRYSTAL_SPIKE = REGISTRY.register("crystal_spike", () -> CrystalSpikeFeature.INSTANCE);
    public static final RegistryObject<Feature<?>> SINGLE_BLOCK = REGISTRY.register("single_block", () -> SingleBlockFeature.INSTANCE);
    public static final RegistryObject<Feature<?>> OBSIDIAN_TOWER = REGISTRY.register("obsidian_tower", () -> ObsidianTowerStructure.INSTANCE);
    public static final RegistryObject<Feature<?>> OBLIVINE = REGISTRY.register("oblivine", () -> OblivineFeature.INSTANCE);
    public static final RegistryObject<Feature<?>> DRALGAE = REGISTRY.register("dralgae", () -> DralgaeFeature.INSTANCE);
    public static final RegistryObject<Feature<?>> HUGE_DRALGAE = REGISTRY.register("huge_dralgae", () -> HugeDralgaeFeature.INSTANCE);
    public static final RegistryObject<Feature<?>> BOULDER = REGISTRY.register("boulder", () -> BoulderFeature.INSTANCE);
    public static final RegistryObject<Feature<?>> CEILING_BOULDER = REGISTRY.register("ceiling_boulder", () -> CeilingBoulderFeature.INSTANCE);
    public static final RegistryObject<Feature<?>> CEILING_SPIKE = REGISTRY.register("ceiling_spike", () -> CeilingSpikeFeature.INSTANCE);
}