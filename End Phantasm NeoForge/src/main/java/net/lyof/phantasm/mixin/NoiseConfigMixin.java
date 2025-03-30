package net.lyof.phantasm.mixin;

import net.lyof.phantasm.world.gen.SamplerHooks;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RandomState.class)
public class NoiseConfigMixin {
	@Shadow
	@Final
	private Climate.Sampler sampler;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(NoiseGeneratorSettings chunkGeneratorSettings, HolderGetter<NormalNoise.NoiseParameters> arg, long seed, CallbackInfo ci) {
		try{
			((SamplerHooks) (Object) sampler).setSeed(seed);
		}catch (ClassCastException e){
			System.out.println("nah... it's fine");
		}
	}
}
