package net.lyof.phantasm.mixin;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lyof.phantasm.world.gen.TheEndBiomeData;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Supplier;

@Mixin(value = TheEndBiomeSource.class,priority = 900)
public class TheEndBiomeSourceMixin extends BiomeSourceMixin {
	@Unique
	private List<Supplier<Object>> overrides = new ArrayList<>();
	@Unique
	private boolean endsPhantasm$biomeMapModified = false;
	@Shadow
	@Mutable
	@Final
    public static MapCodec<TheEndBiomeSource> CODEC;

	/**
	 * Modifies the codec, so it calls the static factory method that gives us access to the
	 * full biome registry instead of just the pre-defined biomes that vanilla uses.
	 */
	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void modifyCodec(CallbackInfo ci) {
		CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(RegistryOps.retrieveGetter(Registries.BIOME)).apply(instance, instance.stable(TheEndBiomeSource::create)));
	}

	/**
	 * Captures the biome registry at the beginning of the static factory method to allow access to it in the
	 * constructor.
	 */
	@Inject(method = "create", at = @At("HEAD"))
	private static void rememberLookup(HolderGetter<Biome> biomes, CallbackInfoReturnable<?> ci) {
		TheEndBiomeData.biomeRegistry.set(biomes);
	}

	/**
	 * Frees up the captured biome registry.
	 */
	@Inject(method = "create", at = @At("TAIL"))
	private static void clearLookup(HolderGetter<Biome> biomes, CallbackInfoReturnable<?> ci) {
		TheEndBiomeData.biomeRegistry.remove();
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void init(Holder<Biome> centerBiome, Holder<Biome> highlandsBiome, Holder<Biome> midlandsBiome, Holder<Biome> smallIslandsBiome, Holder<Biome> barrensBiome, CallbackInfo ci) {
		HolderGetter<Biome> biomes = TheEndBiomeData.biomeRegistry.get();
		if (biomes == null) {
			throw new IllegalStateException("Biome registry not set by Mixin");
		}
		overrides.add(Suppliers.memoize(() -> TheEndBiomeData.createOverrides(biomes)));
	}

	@Inject(method = "getNoiseBiome", at = @At("RETURN"), cancellable = true)
	private void getWeightedEndBiome(int biomeX, int biomeY, int biomeZ, Climate.Sampler noise, CallbackInfoReturnable<Holder<Biome>> cir) {
		try {
			if (!endsPhantasm$biomeMapModified) {
				boolean first = true;
				for (Supplier<Object> mod : overrides) {
					if (!first) {
						List<List<Object>> endbiomes = (List<List<Object>>) mod.get().getClass().getField("endBiomesCopy").get(mod.get());
						List<List<Object>> midlandsbiomes = (List<List<Object>>) mod.get().getClass().getField("midlandsBiomesCopy").get(mod.get());
						List<List<Object>> barrensbiomes = (List<List<Object>>) mod.get().getClass().getField("barrensBiomesCopy").get(mod.get());
						overrides.get(0).get().getClass().getMethod("addExternalModBiomes", List.class, String.class).invoke(overrides.get(0).get(), endbiomes, "end");
						overrides.get(0).get().getClass().getMethod("addExternalModBiomes", List.class, String.class).invoke(overrides.get(0).get(), midlandsbiomes, "midlands");
						overrides.get(0).get().getClass().getMethod("addExternalModBiomes", List.class, String.class).invoke(overrides.get(0).get(), barrensbiomes, "barrens");
					}
					first = false;
				}
				overrides.get(0).get().getClass().getMethod("initBiomes").invoke(overrides.get(0).get());
				endsPhantasm$biomeMapModified = true;
			}
			Supplier<Object> override = overrides.get(0);
			Holder<Biome> picked = (Holder<Biome>) override.get().getClass().getMethod("pick", int.class, int.class, int.class, Climate.Sampler.class, Holder.class).invoke(override.get(), biomeX, biomeY, biomeZ, noise, cir.getReturnValue());
			cir.setReturnValue(picked);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Set<Holder<Biome>> endsPhantasm$modifyBiomeSet(Set<Holder<Biome>> biomes) {
		try {
			var modifiedBiomes = new LinkedHashSet<>(biomes);
			for (Supplier<Object> override : overrides) {
				Field field = override.get().getClass().getField("customBiomes");
				modifiedBiomes.addAll(((Set<Holder<Biome>>) field.get(override.get())));
			}
			return Collections.unmodifiableSet(modifiedBiomes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return biomes;
	}
}
