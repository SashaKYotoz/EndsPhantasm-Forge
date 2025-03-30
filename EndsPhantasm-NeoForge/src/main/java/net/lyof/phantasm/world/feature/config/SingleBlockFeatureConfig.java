package net.lyof.phantasm.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record SingleBlockFeatureConfig(IntProvider y, BlockStateProvider state) implements FeatureConfiguration {
    public static Codec<SingleBlockFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.POSITIVE_CODEC.fieldOf("y_level").forGetter(SingleBlockFeatureConfig::y),
                    net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider.CODEC.fieldOf("block").forGetter(SingleBlockFeatureConfig::state)
            ).apply(instance, SingleBlockFeatureConfig::new));
}