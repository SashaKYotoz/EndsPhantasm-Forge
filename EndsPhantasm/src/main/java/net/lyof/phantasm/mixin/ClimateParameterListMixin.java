package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Pair;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.lyof.phantasm.world.biome.ModBiomes;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = Climate.ParameterList.class, priority = 1100)
public abstract class ClimateParameterListMixin<T> {
    @Shadow
    public abstract List<Pair<Climate.ParameterPoint, T>> values();

    @Unique
    private final List<Pair<Climate.ParameterPoint, T>> endEntries = new ArrayList<>();

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/Climate$RTree;create(Ljava/util/List;)Lnet/minecraft/world/level/biome/Climate$RTree;"))
    public Climate.RTree<T> addEndBiomes(List<Pair<Climate.ParameterPoint, T>> entries, Operation<Climate.RTree<T>> original) {
        Climate.ParameterPoint highlands = null;
        for (Pair<Climate.ParameterPoint, T> e : entries) {
            if (e.getSecond() instanceof Holder r && r.is(Biomes.END_HIGHLANDS))
                highlands = e.getFirst();
        }
        if (highlands != null && ModBiomes.LOOKUP != null) {
            int customCount = EndDataCompat.getEnabledBiomes().size();
            int j = 0;
            for (ResourceKey<Biome> biome : EndDataCompat.getEnabledBiomes()) {
                Phantasm.log("Adding " + biome.location() + " to the End biome source at slice " + (j / 2 + 1) + " out of " + customCount);
                this.endEntries.add(new Pair<>(
                        splitHypercube(highlands, customCount, j),
                        (T) ModBiomes.LOOKUP.getOrThrow(biome)
                ));
                j += 2;
            }
            this.endEntries.addAll(entries.stream()
                    .filter(p -> p.getSecond() instanceof Holder r && !r.is(Biomes.END_HIGHLANDS)
                            && !(r.unwrapKey().get() instanceof Holder k && EndDataCompat.contains(k))).toList());
            for (int i = 1; i <= customCount; i += 2)
                this.endEntries.add(new Pair<>(
                        splitHypercube(highlands, customCount, i),
                        (T) ModBiomes.LOOKUP.getOrThrow(Biomes.END_HIGHLANDS)
                ));
            return Climate.RTree.create(this.endEntries);
        }
        return Climate.RTree.create(entries);
    }

    @Unique
    private static Climate.ParameterPoint splitHypercube(Climate.ParameterPoint base, int biomes, int i) {
        biomes = biomes * 2 - 1;
        //i = i * 2;
        if (EndDataCompat.getCompatibilityMode().equals("endercon"))
            return Climate.parameters(
                    splitRange(base.temperature(), biomes, i),
                    base.humidity(),
                    base.continentalness(),
                    base.erosion(),
                    base.depth(),
                    base.weirdness(),
                    base.offset() / 10000f);

        else if (EndDataCompat.getCompatibilityMode().equals("nullscape"))
            return Climate.parameters(
                    base.temperature(),
                    base.humidity(),
                    base.continentalness(),//splitRange(base.continentalness(), biomes, i),
                    base.erosion(),
                    base.depth(),
                    splitRange(base.weirdness(), biomes, i),
                    base.offset() / 10000f);

        else
            return Climate.parameters(
                    base.temperature(), //splitRange(base.temperature(), biomes, i),
                    base.humidity(), //splitRange(base.humidity(), biomes, i),
                    splitRange(base.continentalness(), biomes, i),
                    base.erosion(),
                    base.depth(), //splitRange(base.depth(), biomes, i),
                    base.weirdness(), //splitRange(base.weirdness(), biomes, i),
                    base.offset() / 10000f);
    }

    @Unique
    private static Climate.Parameter splitRange(Climate.Parameter point, int biomes, int i) {
        long min = (getRange(point) / biomes * i) + point.min();
        long max = (getRange(point) / biomes * (i + 1)) + point.min();
        return Climate.Parameter.span(min / 10000f, max / 10000f);
    }

    @Unique
    private static long getRange(Climate.Parameter point) {
        return point.max() - point.min();
    }

    @ModifyArg(method = "<init>", index = 0, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/Climate$RTree;create(Ljava/util/List;)Lnet/minecraft/world/level/biome/Climate$RTree;"))
    public List<Pair<Climate.ParameterPoint, T>> modifyTree(List<Pair<Climate.ParameterPoint, T>> entries) {
        return this.values();
    }

    @Inject(method = "values", at = @At("HEAD"), cancellable = true)
    public void redirectEntries(CallbackInfoReturnable<List<Pair<Climate.ParameterPoint, T>>> cir) {
        if (!this.endEntries.isEmpty())
            cir.setReturnValue(this.endEntries);
    }
}