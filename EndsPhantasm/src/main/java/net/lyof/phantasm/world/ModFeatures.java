package net.lyof.phantasm.world;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.feature.*;
import net.lyof.phantasm.world.noise.CenterDensityFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURE = DeferredRegister.create(ForgeRegistries.FEATURES, Phantasm.MOD_ID);
    public static final RegistryObject<Feature<?>> CRYSTAL_SPIKE = FEATURE.register("crystal_spike", () -> CrystalSpikeFeature.INSTANCE);
    public static final RegistryObject<Feature<?>> SINGLE_BLOCK = FEATURE.register("single_block", () -> SingleBlockFeature.INSTANCE);
    public static final RegistryObject<Feature<?>> OBSIDIAN_TOWER = FEATURE.register("shattered_tower", () -> ShatteredTowerStructure.INSTANCE);
    public static final RegistryObject<Feature<?>> OBLIVINE = FEATURE.register("oblivine", () -> OblivineFeature.INSTANCE);
    public static final RegistryObject<Feature<?>> DRALGAE = FEATURE.register("dralgae", () -> DralgaeFeature.INSTANCE);
    public static final RegistryObject<Feature<?>> HUGE_DRALGAE = FEATURE.register("huge_dralgae", () -> HugeDralgaeFeature.INSTANCE);
    public static final RegistryObject<Feature<?>> SPIKE = FEATURE.register("spike", () -> SpikeFeature.INSTANCE);
    public static final RegistryObject<Feature<?>> CHORAL = FEATURE.register("choral", () -> ChoralFeature.INSTANCE);
    public static final RegistryObject<Feature<?>> CEILING_SPIKE = FEATURE.register("ceiling_spike", () -> CeilingSpikeFeature.INSTANCE);

    public static final DeferredRegister<Codec<? extends DensityFunction>> FUNCTION = DeferredRegister.create(Registries.DENSITY_FUNCTION_TYPE, Phantasm.MOD_ID);
    public static final RegistryObject<Codec<CenterDensityFunction>> CENTER = FUNCTION.register("center", () -> CenterDensityFunction.CODEC);

    public static void register(IEventBus bus) {
        FEATURE.register(bus);
        FUNCTION.register(bus);
    }
}