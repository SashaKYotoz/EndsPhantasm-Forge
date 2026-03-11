package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.config.Sized2BlockFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class DralgaeFeature extends Feature<Sized2BlockFeatureConfig> {
    public static final Feature<Sized2BlockFeatureConfig> INSTANCE = new DralgaeFeature(Sized2BlockFeatureConfig.CODEC);

    public DralgaeFeature(Codec<Sized2BlockFeatureConfig> configCodec) {
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

        for (int i = 0; i < size; i++) {
            if (originy + i > world.getHeight() || !world.getBlockState(origin.above(i)).isAir()) return true;

            this.setBlock(world, origin.above(i), config.primary().getState(random, origin.above(i)));
        }

        if (random.nextInt(2) == 0)
            this.setBlock(world, origin.above(size), config.secondary().getState(random, origin.above(size)));

        if (Math.random() < 0.7) {
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
}