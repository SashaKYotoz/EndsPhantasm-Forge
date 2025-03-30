package net.lyof.phantasm.world.gen;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.List;

public final class TheEndBiomes {
	private TheEndBiomes() {
	}

	public static void addMainIslandBiome(ResourceKey<Biome> biome, double weight) {
		TheEndBiomeData.addEndBiomeReplacement(Biomes.THE_END, biome, weight);
		TheEndBiomeData.endBiomes.add(List.of(Biomes.THE_END, biome, weight));
	}

	public static void addHighlandsBiome(ResourceKey<Biome> biome, double weight) {
		TheEndBiomeData.addEndBiomeReplacement(Biomes.END_HIGHLANDS, biome, weight);
		TheEndBiomeData.endBiomes.add(List.of(Biomes.END_HIGHLANDS, biome, weight));
	}

	public static void addSmallIslandsBiome(ResourceKey<Biome> biome, double weight) {
		TheEndBiomeData.addEndBiomeReplacement(Biomes.SMALL_END_ISLANDS, biome, weight);
		TheEndBiomeData.endBiomes.add(List.of(Biomes.SMALL_END_ISLANDS, biome, weight));
	}

	public static void addMidlandsBiome(ResourceKey<Biome> highlands, ResourceKey<Biome> midlands, double weight) {
		TheEndBiomeData.addEndMidlandsReplacement(highlands, midlands, weight);
		TheEndBiomeData.midlandsBiomes.add(List.of(highlands, midlands, weight));
	}

	public static void addBarrensBiome(ResourceKey<Biome> highlands, ResourceKey<Biome> barrens, double weight) {
		TheEndBiomeData.addEndBarrensReplacement(highlands, barrens, weight);
		TheEndBiomeData.barrensBiomes.add(List.of(highlands, barrens, weight));
	}
}
