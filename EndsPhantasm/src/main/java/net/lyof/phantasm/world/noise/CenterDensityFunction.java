package net.lyof.phantasm.world.noise;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

public record CenterDensityFunction(int distance) implements DensityFunction.SimpleFunction {
    public static final Codec<CenterDensityFunction> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("distance").forGetter(CenterDensityFunction::distance)
            ).apply(instance, CenterDensityFunction::new));
    public static final KeyDispatchDataCodec<CenterDensityFunction> CODEC_HOLDER = KeyDispatchDataCodec.of(CODEC);


    @Override
    public double compute(FunctionContext pos) {
        float f = pos.blockX();
        float h = pos.blockZ();
        float distance = Mth.sqrt(f * f + h * h);
        return distance > this.distance() ? this.minValue() : this.maxValue();
    }

    @Override
    public double minValue() {
        return 0;
    }

    @Override
    public double maxValue() {
        return 1;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC_HOLDER;
    }
}
