package net.lyof.phantasm.world.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.biome.modifier.FallenStarModifier;
import net.lyof.phantasm.world.biome.modifier.RawPurpureModifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBiomeModifiers {
    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Phantasm.MOD_ID);

    public static final RegistryObject<Codec<RawPurpureModifier>> RAW_PURPURE = BIOME_MODIFIER_SERIALIZERS.register("raw_purpure",
            () -> RecordCodecBuilder.create(builder -> builder.group(
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(RawPurpureModifier::biomes),
                    PlacedFeature.CODEC.fieldOf("features").forGetter(RawPurpureModifier::features)
            ).apply(builder, RawPurpureModifier::new)));
    public static final RegistryObject<Codec<FallenStarModifier>> FALLEN_STAR = BIOME_MODIFIER_SERIALIZERS.register("fallen_star",
            () -> RecordCodecBuilder.create(builder -> builder.group(
                            Biome.LIST_CODEC.fieldOf("biomes").forGetter(FallenStarModifier::biomes),
                            PlacedFeature.CODEC.fieldOf("features").forGetter(FallenStarModifier::features))
                    .apply(builder, FallenStarModifier::new)));
}