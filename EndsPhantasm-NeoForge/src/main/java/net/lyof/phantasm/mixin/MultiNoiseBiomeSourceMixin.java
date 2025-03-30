package net.lyof.phantasm.mixin;

import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.world.biome.ModBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiNoiseBiomeSource.class)
public abstract class MultiNoiseBiomeSourceMixin {
    @Shadow
    protected abstract Climate.ParameterList<Holder<Biome>> parameters();

    @Inject(method = "getNoiseBiome(IIILnet/minecraft/world/level/biome/Climate$Sampler;)Lnet/minecraft/core/Holder;", at = @At("HEAD"), cancellable = true)
    public void forceMainIsland(int x, int y, int z, Climate.Sampler noise, CallbackInfoReturnable<Holder<Biome>> cir) {

        if (!ConfigEntries.forceMainIsland || ModBiomes.LOOKUP == null ||
                this.parameters().values().stream().noneMatch(p -> p.getSecond().is(Biomes.END_HIGHLANDS)))
            return;

        int i = QuartPos.toBlock(x);
        int k = QuartPos.toBlock(z);
        long l = SectionPos.blockToSectionCoord(i);
        long m = SectionPos.blockToSectionCoord(k);
        if (l*l + m*m <= 4096L)
            cir.setReturnValue(ModBiomes.LOOKUP.getOrThrow(Biomes.THE_END));
    }
}