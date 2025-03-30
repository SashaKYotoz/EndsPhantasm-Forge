package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;
import java.util.function.Supplier;

@Mixin(value = BiomeSource.class, priority = 900)
public abstract class BiomeSourceMixin {
    @WrapOperation(method = "possibleBiomes", at = @At(value = "INVOKE", target = "Ljava/util/function/Supplier;get()Ljava/lang/Object;"))
    private Object getBiomes(Supplier instance, Operation<Holder<Biome>> original) {
        return endsPhantasm$modifyBiomeSet((Set<Holder<Biome>>) instance.get());
    }

    @Unique
    protected Set<Holder<Biome>> endsPhantasm$modifyBiomeSet(Set<Holder<Biome>> biomes) {
        return biomes;
    }
}