package net.lyof.phantasm.mixin;

import net.lyof.phantasm.world.biome.ModBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiNoiseBiomeSourceParameterList.class)
public class MultiNoiseBiomeSourceParameterListMixin {
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterList$Preset$SourceProvider;apply(Ljava/util/function/Function;)Lnet/minecraft/world/level/biome/Climate$ParameterList;"))
    private void catchLookup(MultiNoiseBiomeSourceParameterList.Preset preset, HolderGetter<Biome> getter, CallbackInfo ci) {
        ModBiomes.register(getter);
    }
}