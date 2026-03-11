package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.config.Sized2BlockFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class HugeDralgaeFeature extends Feature<Sized2BlockFeatureConfig> {
    public static final Feature<Sized2BlockFeatureConfig> INSTANCE = new HugeDralgaeFeature(Sized2BlockFeatureConfig.CODEC);

    public HugeDralgaeFeature(Codec<Sized2BlockFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Sized2BlockFeatureConfig> context) {
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();
        int originy = origin.getY();
        RandomSource random = context.random();
        Sized2BlockFeatureConfig config = context.config();

        if (!world.getBlockState(origin.below()).is(ModTags.Blocks.DRALGAE_GROWABLE_ON)) return false;

        int size = config.size().sample(random);
        Direction dir = Direction.getRandom(random);
        if (dir.getAxis() == Direction.Axis.Y) dir = dir.getClockWise(Direction.Axis.X);

        for (int i = 0; i < size; i++) {
            if (originy + i > world.getHeight() || !world.getBlockState(origin.above(i)).isAir()) return true;

            this.setBlock(world, origin.above(i), config.primary().getState(random, origin.above(i)));
            if (i >= 3 && i % 2 == 0 && i < size - 3) {
                this.setBlock(world, origin.above(i).relative(dir), config.secondary().getState(random, origin.above(i)));
                dir = dir.getOpposite();
            }
        }

        return true;
    }
}