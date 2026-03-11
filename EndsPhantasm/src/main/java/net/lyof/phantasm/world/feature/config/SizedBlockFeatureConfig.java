package net.lyof.phantasm.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record SizedBlockFeatureConfig(IntProvider size, BlockStateProvider block) implements FeatureConfiguration {
    public static Codec<SizedBlockFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.NON_NEGATIVE_CODEC.fieldOf("size").forGetter(SizedBlockFeatureConfig::size),
                    BlockStateProvider.CODEC.fieldOf("block").forGetter(SizedBlockFeatureConfig::block)
            ).apply(instance, SizedBlockFeatureConfig::new));
}