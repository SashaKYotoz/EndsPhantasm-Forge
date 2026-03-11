package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.config.SizedBlockFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.HashMap;
import java.util.Map;

public class SpikeFeature extends Feature<SizedBlockFeatureConfig> {
    public static final Feature<SizedBlockFeatureConfig> INSTANCE = new SpikeFeature(SizedBlockFeatureConfig.CODEC);

    public SpikeFeature(Codec<SizedBlockFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SizedBlockFeatureConfig> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        SizedBlockFeatureConfig config = context.config();
        this.generate(level, origin, config, random, 0);
        return true;
    }

    public void generate(WorldGenLevel world, BlockPos pos, SizedBlockFeatureConfig config, RandomSource random, int spread) {

        if (!world.getBlockState(pos.below()).is(ModTags.Blocks.END_PLANTS_GROWABLE_ON))
            return;

        int size = config.size().sample(random) - spread;
        if (size < 0) return;

        Map<Direction, Integer> sizes = new HashMap<>();
        if (size < random.nextIntBetweenInclusive(6, 8)) for (Direction dir : Direction.values())
            sizes.put(dir, -2);
        else for (Direction dir : Direction.values())
            sizes.put(dir, size - random.nextIntBetweenInclusive(2, 4));

        pos = pos.below(2);
        for (int i = -2; i < size; i++) {
            BlockPos finalPos = pos;
            this.safeSetBlock(world, pos, config.block().getState(random, pos), block -> block.propagatesSkylightDown(world, finalPos));
            for (int k = -0; k < 4; k++) {
                if (i < sizes.get(Direction.from2DDataValue(k)))
                    this.safeSetBlock(world, pos.relative(Direction.from2DDataValue(k)), config.block().getState(random, pos),
                            block -> block.propagatesSkylightDown(world, finalPos));
            }

            pos = pos.above();
        }

        if (random.nextDouble() < 0.8) {
            BlockPos newPos = this.findY(world, pos.east(random.nextIntBetweenInclusive(-5, 5)).north(random.nextIntBetweenInclusive(-5, 5)));
            if (newPos != null)
                this.generate(world, newPos, config, random, spread + 1);
        }
        if (spread < 2) {
            BlockPos newPos = this.findY(world, pos.east(random.nextIntBetweenInclusive(-5, 5)).north(random.nextIntBetweenInclusive(-5, 5)));
            if (newPos != null)
                this.generate(world, newPos, config, random, spread + 1);
        }
    }

    public BlockPos findY(WorldGenLevel world, BlockPos start) {
        BlockPos pos;
        for (int i = 0; i < 20; i++) {
            pos = start.atY((int) (start.getY() + Math.pow(-1, i) * i / 2));
            if (world.getBlockState(pos).canBeReplaced() && world.getBlockState(pos.below()).is(ModTags.Blocks.END_PLANTS_GROWABLE_ON)
                    && !world.getBlockState(pos.below()).is(ModBlocks.CIRITE.get()))
                return pos;
        }
        return null;
    }
}