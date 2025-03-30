package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;

public class OblivineFeature extends Feature<BlockColumnConfiguration> {
    public static final Feature<BlockColumnConfiguration> INSTANCE = new OblivineFeature(BlockColumnConfiguration.CODEC);

    public OblivineFeature(Codec<BlockColumnConfiguration> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockColumnConfiguration> context) {
        LevelAccessor accessor = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        BlockColumnConfiguration config = context.config();
        int size = accessor.getRandom().nextIntBetweenInclusive(4, 12);

        BlockPos pos = new BlockPos(origin).atY(0);
        while (pos.getY() < accessor.getHeight() && !(accessor.getBlockState(pos.above()).is(ModBlocks.OBLIVION.get())
                && accessor.getBlockState(pos).is(Blocks.AIR))) {

            pos = pos.above();
        }

        for (int i = 0; i < size; i++) {
            if (pos.getY() < accessor.getMinBuildHeight() || pos.getY() > 250)
                return true;

            BlockState state = config.layers().get(0).state().getState(random, pos);
            accessor.setBlock(pos, state, 3);
            pos = pos.below();
        }
        if (random.nextInt(5) == 0)
            accessor.setBlock(pos, ModBlocks.CRYSTALILY.get().defaultBlockState(), 3);

        if (Math.random() < 0.9) {
            FeaturePlaceContext<BlockColumnConfiguration> contextnext =
                    new FeaturePlaceContext<>(context.topFeature(),
                            context.level(),
                            context.chunkGenerator(),
                            context.random(),
                            context.origin().east(random.nextIntBetweenInclusive(-5, 5)).north(random.nextIntBetweenInclusive(-5, 5)),
                            config);
            this.place(contextnext);
        }
        return true;
    }
}