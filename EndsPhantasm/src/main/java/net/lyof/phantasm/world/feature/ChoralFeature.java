package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.block.custom.DirectionalBlock;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.config.Sized2BlockFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.List;

public class ChoralFeature extends Feature<Sized2BlockFeatureConfig> {
    public static final Feature<Sized2BlockFeatureConfig> INSTANCE = new ChoralFeature(Sized2BlockFeatureConfig.CODEC);

    public ChoralFeature(Codec<Sized2BlockFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Sized2BlockFeatureConfig> context) {
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        Sized2BlockFeatureConfig config = context.config();

        List<BlockPos> toPlace = new ArrayList<>();
        List<BlockPos> fans = new ArrayList<>();

        int size = config.size().sample(random);
        Direction primary = Direction.from2DDataValue(random.nextInt(4));
        Direction secondary = random.nextBoolean() ? primary.getClockWise() : primary.getCounterClockWise();

        BlockPos pos = new BlockPos(origin).atY(0);
        while (pos.getY() < world.getHeight() && !(world.getBlockState(pos.above()).is(ModTags.Blocks.END_PLANTS_GROWABLE_ON)
                && world.getBlockState(pos).is(Blocks.AIR))) {

            pos = pos.above();
        }
        if (pos.getY() >= world.getHeight() - 2) return false;

        this.spike(toPlace, pos, 1);
        this.spike(fans, pos, 2);
        pos = this.move(pos, primary, secondary, random, world);
        for (int i = 0; i < size; i++) {
            this.spike(toPlace, pos, 2);
            this.spike(fans, pos, 3);
            pos = this.move(pos, primary, secondary, random, world);
        }
        this.spike(toPlace, pos, 1);
        this.spike(fans, pos, 2);

        for (BlockPos p : toPlace)
            this.safeSetBlock(world, p, config.primary().getState(random, p),
                    block -> size > 0 && block.propagatesSkylightDown(world, p));
        for (BlockPos p : fans) {
            BlockState state = config.secondary().getState(random, p);
            if (state.getBlock() instanceof DirectionalBlock directional)
                state = directional.getPlacementState(world, p);
            this.safeSetBlock(world, p, state,
                    block -> random.nextInt(5) == 0 && block.propagatesSkylightDown(world, p));
        }


        if (size == 0 && Math.random() < 0.7) {
            FeaturePlaceContext<Sized2BlockFeatureConfig> contextnext =
                    new FeaturePlaceContext<>(context.topFeature(),
                            context.level(),
                            context.chunkGenerator(),
                            context.random(),
                            context.origin().east(random.nextIntBetweenInclusive(-5, 5)).north(random.nextIntBetweenInclusive(-5, 5)),
                            config);
            if (world.hasChunkAt(contextnext.origin()))
                this.place(contextnext);
        }

        return true;
    }

    public BlockPos move(BlockPos pos, Direction primary, Direction secondary, RandomSource random, WorldGenLevel world) {
        pos = pos.relative(primary);

        if (random.nextInt(4) == 0) pos = pos.relative(primary.getClockWise());
        else if (random.nextInt(4) == 0) pos = pos.relative(primary.getCounterClockWise());
        else if (random.nextInt(4) == 0) pos = pos.relative(secondary);

        if (!world.getBlockState(pos).propagatesSkylightDown(world, pos))
            pos = pos.below();
        if (world.getBlockState(pos.above()).propagatesSkylightDown(world, pos))
            pos = pos.above();

        return pos;
    }

    public void spike(List<BlockPos> world, BlockPos pos, int layer) {
        if (layer <= 0) {
            if (!world.contains(pos)) world.add(pos);
            return;
        }

        for (Direction dir : Direction.values())
            this.spike(world, pos.relative(dir), layer - 1);
    }
}