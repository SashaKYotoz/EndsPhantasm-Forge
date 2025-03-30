package net.lyof.phantasm.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;
import java.util.function.Supplier;

@Mixin({BiomeSource.class})
public abstract class BiomeSourceMixin {
	@Redirect(method = "possibleBiomes", at = @At(value = "INVOKE", target = "Ljava/util/function/Supplier;get()Ljava/lang/Object;"))
	private Object getBiomes(Supplier<Set<Holder<Biome>>> instance) {
		return endsPhantasm$modifyBiomeSet(instance.get());
	}

	@Unique
	public Set<Holder<Biome>> endsPhantasm$modifyBiomeSet(Set<Holder<Biome>> biomes) {
		return biomes;
	}
}