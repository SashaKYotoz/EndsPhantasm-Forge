package net.lyof.phantasm.world.biome.modifier;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.world.biome.ModBiomeModifiers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo.BiomeInfo.Builder;

public record RawPurpureModifier(HolderSet<Biome> biomes, Holder<PlacedFeature> features) implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, Builder builder) {
        if (phase == Phase.ADD && this.biomes.contains(biome) && ConfigEntries.doRawPurpur)
            builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES.ordinal(), features);
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return ModBiomeModifiers.RAW_PURPURE.get();
    }
}