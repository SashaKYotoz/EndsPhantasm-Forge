package net.lyof.phantasm.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record CrystalSpikeFeatureConfig(IntProvider size, float voidChance) implements FeatureConfiguration {
    public static Codec<CrystalSpikeFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.POSITIVE_CODEC.fieldOf("size").forGetter(CrystalSpikeFeatureConfig::size),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("void_chance").forGetter(CrystalSpikeFeatureConfig::voidChance)
            ).apply(instance, CrystalSpikeFeatureConfig::new));
}