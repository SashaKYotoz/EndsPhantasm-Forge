package net.lyof.phantasm.mixin;

import com.google.common.base.Preconditions;
import net.lyof.phantasm.world.gen.SamplerHooks;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Climate.Sampler.class)
public class ClimateSamplerMixin implements SamplerHooks {
	@Unique
	private Long endsPhantasm$seed = null;
	@Unique
	private ImprovedNoise endsPhantasm$endBiomesSampler = null;

	@Override
	public void setSeed(long seed) {
		this.endsPhantasm$seed = seed;
	}

	@Override
	public long getSeed() {
		return this.endsPhantasm$seed;
	}

	@Override
	public ImprovedNoise getEndBiomesSampler() {
		if (endsPhantasm$endBiomesSampler == null) {
			Preconditions.checkState(endsPhantasm$seed != null, "MultiNoiseSampler doesn't have a seed set, created using different method?");
			endsPhantasm$endBiomesSampler = new ImprovedNoise(new WorldgenRandom(new LegacyRandomSource(endsPhantasm$seed)));
		}
		return endsPhantasm$endBiomesSampler;
	}
}
