package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;
import java.util.function.Supplier;

@Mixin({BiomeSource.class})
public abstract class BiomeSourceMixin {
    @WrapOperation(method = "possibleBiomes", at = @At(value = "INVOKE", target = "Ljava/util/function/Supplier;get()Ljava/lang/Object;"))
    private Object getBiomes(Supplier<Set<Holder<Biome>>> instance, Operation<Object> original) {
        return endsPhantasm$modifyBiomeSet(instance.get());
    }

    @Unique
    public Set<Holder<Biome>> endsPhantasm$modifyBiomeSet(Set<Holder<Biome>> biomes) {
        return biomes;
    }
}