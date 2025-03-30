package net.lyof.phantasm.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record DralgaeFeatureConfig(IntProvider size, BlockStateProvider stem,
                                   BlockStateProvider fruit) implements FeatureConfiguration {
    public static Codec<DralgaeFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.POSITIVE_CODEC.fieldOf("size").forGetter(DralgaeFeatureConfig::size),
                    BlockStateProvider.CODEC.fieldOf("stem").forGetter(DralgaeFeatureConfig::stem),
                    BlockStateProvider.CODEC.fieldOf("fruit").forGetter(DralgaeFeatureConfig::fruit)
            ).apply(instance, DralgaeFeatureConfig::new));
}