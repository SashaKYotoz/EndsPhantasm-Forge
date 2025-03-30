package net.lyof.phantasm.mixin;

import net.lyof.phantasm.world.biome.EndDataCompat;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseRouter.class)
public abstract class NoiseRouterMixin {
    @Unique
    private static final DensityFunction END =
            DensityFunctions.add(
                    DensityFunctions.mul(
                            DensityFunctions.add(
                                    DensityFunctions.endIslands(0),
                                    DensityFunctions.constant(0.84375)),
                            DensityFunctions.constant(2 / (0.5625 + 0.84375))),
                    DensityFunctions.constant(-1));

    @Shadow
    @Final
    private DensityFunction temperature;

    @Inject(method = "temperature", at = @At("HEAD"), cancellable = true)
    public void overrideEndTemperature(CallbackInfoReturnable<DensityFunction> cir) {
        if (EndDataCompat.getCompatibilityMode().equals("endercon")
                && this.temperature.minValue() == 0 && this.temperature.minValue() == 0) {
            cir.setReturnValue(END);
        }
    }
}
