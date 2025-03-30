package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.config.DralgaeFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class HugeDralgaeFeature extends Feature<DralgaeFeatureConfig> {
    public static final Feature<DralgaeFeatureConfig> INSTANCE = new HugeDralgaeFeature(DralgaeFeatureConfig.CODEC);

    public HugeDralgaeFeature(Codec<DralgaeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<DralgaeFeatureConfig> context) {
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();
        int originy = origin.getY();
        RandomSource random = context.random();
        DralgaeFeatureConfig config = context.config();

        if (!world.getBlockState(origin.below()).is(ModTags.Blocks.DRALGAE_GROWABLE_ON)) return false;

        int size = config.size().sample(random);
        Direction dir = Direction.getRandom(random);
        if (dir.getAxis() == Direction.Axis.Y) dir = dir.getClockWise(Direction.Axis.X);

        for (int i = 0; i < size; i++) {
            if (originy + i > world.getHeight() || !world.getBlockState(origin.above(i)).isAir()) return true;

            this.setBlock(world, origin.above(i), config.stem().getState(random, origin.above(i)));
            if (i >= 3 && i % 2 == 0 && i < size - 3) {
                this.setBlock(world, origin.above(i).relative(dir), config.fruit().getState(random, origin.above(i)));
                dir = dir.getOpposite();
            }
        }

        return true;
    }
}