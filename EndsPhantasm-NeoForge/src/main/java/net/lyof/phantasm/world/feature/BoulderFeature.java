package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.config.BoulderFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.List;

public class BoulderFeature extends Feature<BoulderFeatureConfig> {
    public static final Feature<BoulderFeatureConfig> INSTANCE = new BoulderFeature(BoulderFeatureConfig.CODEC);

    public BoulderFeature(Codec<BoulderFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BoulderFeatureConfig> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        BoulderFeatureConfig config = context.config();

        if (!level.getBlockState(origin.below()).is(ModTags.Blocks.END_PLANTS_GROWABLE_ON))
            return false;

        List<BlockPos> toPlace = new ArrayList<>();

        BlockPos pos = origin.mutable();
        int size = config.size().sample(random);
        Direction primary = Direction.from2DDataValue(random.nextInt(4));
        Direction secondary = random.nextBoolean() ? primary.getClockWise() : primary.getCounterClockWise();
        boolean tall = random.nextBoolean();

        this.spike(toPlace, pos, 1, tall);
        pos = this.move(pos, primary, secondary, random, level);
        for (int i = 0; i < size; i++) {
            this.spike(toPlace, pos, 2, tall);
            pos = this.move(pos, primary, secondary, random, level);
        }
        this.spike(toPlace, pos, 1, tall);

        for (BlockPos place : toPlace)
            this.safeSetBlock(level, place, config.block().getState(random, place), block -> block.propagatesSkylightDown(level, place));

        return true;
    }

    public BlockPos move(BlockPos pos, Direction primary, Direction secondary, RandomSource random, WorldGenLevel world) {
        pos = pos.relative(primary);

        if (random.nextInt(4) == 0) pos = pos.relative(primary.getClockWise());
        else if (random.nextInt(4) == 0) pos = pos.relative(primary.getCounterClockWise());
        else if (random.nextInt(4) == 0) pos = pos.relative(secondary);

        if (!world.getBlockState(pos).propagatesSkylightDown(world, pos))
            pos = pos.above();
        if (world.getBlockState(pos.below()).propagatesSkylightDown(world, pos))
            pos = pos.below();

        return pos;
    }

    public void spike(List<BlockPos> world, BlockPos pos, int layer, boolean tall) {
        if (layer <= 0) {
            if (!world.contains(pos)) world.add(pos);
            if (!world.contains(pos.above())) world.add(pos.above());
            if (tall && !world.contains(pos.above(2))) world.add(pos.above(2));
            if (!world.contains(pos.below())) world.add(pos.below());
            return;
        }

        for (Direction dir : Direction.values())
            this.spike(world, pos.relative(dir), layer - 1, tall);
    }
}