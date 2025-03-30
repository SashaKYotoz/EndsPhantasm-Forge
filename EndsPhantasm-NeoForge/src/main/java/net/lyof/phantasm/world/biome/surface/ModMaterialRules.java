package net.lyof.phantasm.world.biome.surface;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.mixin.access.NoiseGeneratorSettingsAccess;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.lyof.phantasm.world.biome.ModBiomes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@EventBusSubscriber
public class ModMaterialRules {
    public static SurfaceRules.RuleSource createDreamingDenRule() {
        double min_noise = -0.4;

        SurfaceRules.ConditionSource is_dreaming_den = SurfaceRules.isBiome(ModBiomes.DREAMING_DEN);
        SurfaceRules.ConditionSource is_acidburnt_abysses = SurfaceRules.isBiome(ModBiomes.ACIDBURNT_ABYSSES);

        SurfaceRules.ConditionSource nihilium_noise_main =
                SurfaceRules.noiseCondition(Noises.AQUIFER_FLUID_LEVEL_SPREAD, min_noise, 0);
        SurfaceRules.ConditionSource nihilium_noise_sub =
                SurfaceRules.noiseCondition(Noises.SWAMP, -0.1);

        SurfaceRules.ConditionSource band_noise =
                SurfaceRules.noiseCondition(Noises.SURFACE, 0);
        int raw_purpur_offset = EndDataCompat.getCompatibilityMode().equals("endercon") ? 20 : 0;
        raw_purpur_offset += ConfigEntries.rawPurpurOffset;
        SurfaceRules.ConditionSource band_y_below = SurfaceRules.verticalGradient("raw_purpur_stripes_below1",
                VerticalAnchor.absolute(raw_purpur_offset + 40), VerticalAnchor.absolute(raw_purpur_offset + 42));
        SurfaceRules.ConditionSource band_y_above = SurfaceRules.not(SurfaceRules.verticalGradient("raw_purpur_stripes_above1",
                VerticalAnchor.absolute(raw_purpur_offset + 35), VerticalAnchor.absolute(raw_purpur_offset + 37)));

        SurfaceRules.ConditionSource band_y_below2 = SurfaceRules.verticalGradient("raw_purpur_stripes_below2",
                VerticalAnchor.absolute(raw_purpur_offset + 32), VerticalAnchor.absolute(34));
        SurfaceRules.ConditionSource band_y_above2 = SurfaceRules.not(SurfaceRules.verticalGradient("raw_purpur_stripes_above2",
                VerticalAnchor.absolute(raw_purpur_offset + 27), VerticalAnchor.absolute(raw_purpur_offset + 29)));

        SurfaceRules.ConditionSource band_y_below3 = SurfaceRules.verticalGradient("raw_purpur_stripes_below3",
                VerticalAnchor.absolute(raw_purpur_offset + 24), VerticalAnchor.absolute(raw_purpur_offset + 26));
        SurfaceRules.ConditionSource band_y_above3 = SurfaceRules.not(SurfaceRules.verticalGradient(raw_purpur_offset + "raw_purpur_stripes_above3",
                VerticalAnchor.absolute(raw_purpur_offset + 19), VerticalAnchor.absolute(raw_purpur_offset + 21)));

        // RAW PURPUR RULES
        SurfaceRules.RuleSource raw_purpur_stripes =
                SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.isBiome(Biomes.THE_END)),
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(
                                        band_y_above,
                                        SurfaceRules.ifTrue(band_y_below,
                                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.VEGETATION,
                                                                0.1),
                                                        SurfaceRules.state(ModBlocks.RAW_PURPUR.get().defaultBlockState())
                                                )
                                        )
                                ), SurfaceRules.ifTrue(
                                        band_y_above2,
                                        SurfaceRules.ifTrue(band_y_below2,
                                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.CALCITE,
                                                                0),
                                                        SurfaceRules.state(ModBlocks.RAW_PURPUR.get().defaultBlockState())
                                                )
                                        )
                                ), SurfaceRules.ifTrue(
                                        band_y_above3,
                                        SurfaceRules.ifTrue(band_y_below3,
                                                SurfaceRules.ifTrue(band_noise,
                                                        SurfaceRules.state(ModBlocks.RAW_PURPUR.get().defaultBlockState())
                                                )
                                        )
                                )
                        )
                );

        // DREAMING DEN RULES
        SurfaceRules.RuleSource dreaming_den_nihilium = SurfaceRules.ifTrue(
                is_dreaming_den,
                SurfaceRules.ifTrue(
                        SurfaceRules.ON_FLOOR,
//                        SurfaceRules.ifTrue(
//                                SurfaceRules.yStartCheck(VerticalAnchor.aboveBottom(48), 0),
                        SurfaceRules.state(ModBlocks.VIVID_NIHILIUM.get().defaultBlockState())
//                        )
                )
        );
        SurfaceRules.RuleSource oblivion = SurfaceRules.ifTrue(
                SurfaceRules.yBlockCheck(VerticalAnchor.belowTop(220), 0),
                SurfaceRules.ifTrue(
                        SurfaceRules.stoneDepthCheck(2, false, CaveSurface.CEILING),
                        SurfaceRules.state(ModBlocks.OBLIVION.get().defaultBlockState())
                )
        );

        SurfaceRules.RuleSource dreaming_den = SurfaceRules.sequence(
                SurfaceRules.ifTrue(
                        nihilium_noise_main,
                        dreaming_den_nihilium
                ),
                SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(Noises.AQUIFER_FLUID_LEVEL_SPREAD, min_noise),
                        SurfaceRules.ifTrue(
                                nihilium_noise_sub,
                                dreaming_den_nihilium
                        )
                ),
                SurfaceRules.ifTrue(
                        is_dreaming_den,
                        SurfaceRules.ifTrue(
                                nihilium_noise_main,
                                oblivion
                        )
                ),
                SurfaceRules.ifTrue(
                        is_dreaming_den,
                        SurfaceRules.ifTrue(
                                nihilium_noise_sub,
                                oblivion
                        )
                )
        );
        // ACIDBURNT ABYSSES RULES
        SurfaceRules.RuleSource acidburnt_abysses_nihilium = SurfaceRules.ifTrue(
                is_acidburnt_abysses,
                SurfaceRules.ifTrue(
                        SurfaceRules.ON_FLOOR,
                        SurfaceRules.state(ModBlocks.ACIDIC_NIHILIUM.get().defaultBlockState())
                )
        );

        SurfaceRules.RuleSource acidburnt_abysses_mass = SurfaceRules.ifTrue(
                is_acidburnt_abysses,
                SurfaceRules.ifTrue(
                        SurfaceRules.stoneDepthCheck(2, false, CaveSurface.FLOOR),
                        SurfaceRules.ifTrue(
                                SurfaceRules.yBlockCheck(VerticalAnchor.aboveBottom(50), 0),
                                SurfaceRules.state(ModBlocks.ACIDIC_MASS.get().defaultBlockState())
                        )
                )
        );
        SurfaceRules.RuleSource acidburnt_abysses = SurfaceRules.sequence(
                SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(Noises.BADLANDS_PILLAR, 0.25),
                        acidburnt_abysses_mass
                ),
                SurfaceRules.ifTrue(
                        nihilium_noise_main,
                        acidburnt_abysses_nihilium
                ),
                SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(Noises.AQUIFER_FLUID_LEVEL_SPREAD, min_noise),
                        SurfaceRules.ifTrue(
                                nihilium_noise_sub,
                                acidburnt_abysses_nihilium
                        )
                )
        );

        return ConfigEntries.doRawPurpur ?
                SurfaceRules.sequence(
                        dreaming_den,
                        acidburnt_abysses,
                        raw_purpur_stripes
                ) : SurfaceRules.sequence(
                dreaming_den,
                acidburnt_abysses);
    }

    @SubscribeEvent
    public static void addModMaterialRules(ServerAboutToStartEvent event) {
        LevelStem levelStem = event.getServer().registryAccess().registryOrThrow(Registries.LEVEL_STEM).get(LevelStem.END);
        if (levelStem != null) {
            ChunkGenerator chunkGenerator = levelStem.generator();
            boolean hasEndBiomes = chunkGenerator.getBiomeSource().possibleBiomes().stream().anyMatch(biomeHolder -> biomeHolder.unwrapKey().orElseThrow().location().getNamespace().equals("phantasm"));
            if (hasEndBiomes) {
                if (chunkGenerator instanceof NoiseBasedChunkGenerator generator) {
                    NoiseGeneratorSettings settings = generator.generatorSettings().getDelegate().value();
                    ((NoiseGeneratorSettingsAccess) (Object) settings).addSurfaceRule(
                            SurfaceRules.sequence(
                                    ModMaterialRules.createDreamingDenRule(), generator.generatorSettings().getDelegate().value().surfaceRule()
                            )
                    );
                }
            }
        }
    }
}
