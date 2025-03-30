package net.lyof.phantasm.world.feature.tree;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.feature.tree.custom.PreamTrunkPlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTrunkTypes {
    public static DeferredRegister<TrunkPlacerType<?>> TRUNK_TYPE_REGISTRY = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, Phantasm.MOD_ID);
    public static final RegistryObject<TrunkPlacerType<?>> PREAM_TRUNK_PLACER = TRUNK_TYPE_REGISTRY.register("pream_trunk_placer",
            ()-> new TrunkPlacerType<>(PreamTrunkPlacer.CODEC));
}
