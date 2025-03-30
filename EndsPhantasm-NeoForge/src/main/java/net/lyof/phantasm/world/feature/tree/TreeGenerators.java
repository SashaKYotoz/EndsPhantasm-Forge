package net.lyof.phantasm.world.feature.tree;

import net.lyof.phantasm.world.ModConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class TreeGenerators {
    public static final TreeGrower PREAM = new TreeGrower("phantasm:pream",
            0.25f,
            Optional.empty(),
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.PREAM),
            Optional.of(ModConfiguredFeatures.TALL_PREAM),
            Optional.empty(),
            Optional.empty());
}