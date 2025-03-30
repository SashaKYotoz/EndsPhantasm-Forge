package net.lyof.phantasm.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record PurpurCabinFeatureConfig(IntProvider width, IntProvider length) implements FeatureConfiguration {
    public static Codec<PurpurCabinFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.POSITIVE_CODEC.fieldOf("width").forGetter(PurpurCabinFeatureConfig::width),
                    IntProvider.POSITIVE_CODEC.fieldOf("length").forGetter(PurpurCabinFeatureConfig::length)
            ).apply(instance, PurpurCabinFeatureConfig::new));
}