package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.world.feature.config.SingleBlockFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SingleBlockFeature extends Feature<SingleBlockFeatureConfig> {
    public static final Feature<SingleBlockFeatureConfig> INSTANCE = new SingleBlockFeature(SingleBlockFeatureConfig.CODEC);

    public SingleBlockFeature(Codec<SingleBlockFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SingleBlockFeatureConfig> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();
        SingleBlockFeatureConfig config = context.config();
        int y = config.y().sample(random);
        BlockState state = config.state().getState(random,pos);
        level.setBlock(pos.atY(y), state, 2);
        return ConfigEntries.doFallenStars;
    }
}