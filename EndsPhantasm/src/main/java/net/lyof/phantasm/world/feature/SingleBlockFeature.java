package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.world.feature.config.SizedBlockFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SingleBlockFeature extends Feature<SizedBlockFeatureConfig> {
    public static final Feature<SizedBlockFeatureConfig> INSTANCE = new SingleBlockFeature(SizedBlockFeatureConfig.CODEC);

    public SingleBlockFeature(Codec<SizedBlockFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SizedBlockFeatureConfig> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();
        SizedBlockFeatureConfig config = context.config();
        this.safeSetBlock(level, pos.atY(config.size().sample(random)), config.block().getState(random, pos), BlockBehaviour.BlockStateBase::canBeReplaced);
        return ConfigEntries.doFallenStars;
    }
}