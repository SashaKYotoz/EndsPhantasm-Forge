package net.lyof.phantasm.world.feature.tree;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.feature.tree.custom.PreamFoliagePlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModFoliageTypes {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_REGISTRY = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, Phantasm.MOD_ID);
    public static final RegistryObject<FoliagePlacerType<?>> PREAM_FOLIAGE_PLACER = FOLIAGE_PLACER_REGISTRY.register("pream_foliage_placer",()-> new FoliagePlacerType<>(PreamFoliagePlacer.CODEC));
}
