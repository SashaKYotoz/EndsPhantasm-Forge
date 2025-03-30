package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.config.BoulderFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CeilingSpikeFeature extends Feature<BoulderFeatureConfig> {
    public static final Feature<BoulderFeatureConfig> INSTANCE = new CeilingSpikeFeature(BoulderFeatureConfig.CODEC);

    public CeilingSpikeFeature(Codec<BoulderFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BoulderFeatureConfig> context) {
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        BoulderFeatureConfig config = context.config();

        int size = config.size().sample(random);

        List<BlockPos> toPlace = new ArrayList<>();
        Map<Direction, Integer> sizes = new HashMap<>();
        for (Direction dir : Direction.values())
            sizes.put(dir, size - random.nextIntBetweenInclusive(2, 4));


        BlockPos pos = new BlockPos(origin).atY(0);
        while (pos.getY() < world.getHeight() && !(world.getBlockState(pos.above()).is(ModTags.Blocks.END_PLANTS_GROWABLE_ON)
                && world.getBlockState(pos).is(Blocks.AIR))) {

            pos = pos.above();
        }
        pos = pos.above();
        if (pos.getY() >= world.getHeight() - 2) return false;

        for (int i = 0; i < size; i++) {
            BlockPos finalPos = pos;
            this.safeSetBlock(world, pos, config.block().getState(random, pos), block -> block.propagatesSkylightDown(world, finalPos));
            for (int k = 0; k < 4; k++) {
                if (i < sizes.get(Direction.from2DDataValue(k)))
                    this.safeSetBlock(world, pos.relative(Direction.from2DDataValue(k)), config.block().getState(random, pos),
                            block -> block.propagatesSkylightDown(world, finalPos));
            }

            pos = pos.below();
        }

        return true;
    }
}