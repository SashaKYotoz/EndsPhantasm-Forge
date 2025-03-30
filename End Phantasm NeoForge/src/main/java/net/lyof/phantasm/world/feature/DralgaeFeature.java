package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.config.DralgaeFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class DralgaeFeature extends Feature<DralgaeFeatureConfig> {
    public static final Feature<DralgaeFeatureConfig> INSTANCE = new DralgaeFeature(DralgaeFeatureConfig.CODEC);

    public DralgaeFeature(Codec<DralgaeFeatureConfig> configCodec) {
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

        for (int i = 0; i < size; i++) {
            if (originy + i > world.getHeight() || !world.getBlockState(origin.above(i)).isAir()) return true;

            this.setBlock(world, origin.above(i), config.stem().getState(random, origin.above(i)));
        }

        if (random.nextInt(2) == 0)
            this.setBlock(world, origin.above(size), config.fruit().getState(random, origin.above(size)));

        if (Math.random() < 0.7) {
            FeaturePlaceContext<DralgaeFeatureConfig> contextnext =
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