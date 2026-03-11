package net.lyof.phantasm.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record Sized2BlockFeatureConfig(IntProvider size, BlockStateProvider primary, BlockStateProvider secondary) implements FeatureConfiguration {
    public static Codec<Sized2BlockFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.NON_NEGATIVE_CODEC.fieldOf("size").forGetter(Sized2BlockFeatureConfig::size),
                    BlockStateProvider.CODEC.fieldOf("primary").forGetter(Sized2BlockFeatureConfig::primary),
                    BlockStateProvider.CODEC.fieldOf("secondary").forGetter(Sized2BlockFeatureConfig::secondary)
            ).apply(instance, Sized2BlockFeatureConfig::new));
}