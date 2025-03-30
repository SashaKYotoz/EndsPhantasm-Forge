package net.lyof.phantasm.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ShatteredPendantData(int x, int y, int z, String dimensionId) {
    public static final Codec<ShatteredPendantData> CODEC;

    static {
        CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Codec.INT.fieldOf("x").forGetter(ShatteredPendantData::x),
                        Codec.INT.fieldOf("y").forGetter(ShatteredPendantData::y),
                        Codec.INT.fieldOf("z").forGetter(ShatteredPendantData::z),
                        Codec.STRING.fieldOf("dimensionId").forGetter(ShatteredPendantData::dimensionId)
                ).apply(instance, ShatteredPendantData::new)
        );
    }
}