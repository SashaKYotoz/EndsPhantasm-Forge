package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.custom.CrystalShardBlock;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.config.CrystalSpikeFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class CrystalSpikeFeature extends Feature<CrystalSpikeFeatureConfig> {
    public static final Feature<CrystalSpikeFeatureConfig> INSTANCE = new CrystalSpikeFeature(CrystalSpikeFeatureConfig.CODEC);

    public CrystalSpikeFeature(Codec<CrystalSpikeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<CrystalSpikeFeatureConfig> placeContext) {
        WorldGenLevel world = placeContext.level();
        BlockPos origin = placeContext.origin();
        RandomSource random = placeContext.random();
        CrystalSpikeFeatureConfig config = placeContext.config();

        if (!world.getBiome(origin).is(ModTags.Biomes.DREAMING_DEN))
            return false;

        int size = config.size().sample(random);
        float chance = config.voidChance();

        BlockState top = ModBlocks.CRYSTAL_SHARD.get().defaultBlockState();
        BlockState bottom = random.nextFloat() < chance
                ? ModBlocks.VOID_CRYSTAL_SHARD.get().defaultBlockState()
                : ModBlocks.CRYSTAL_SHARD.get().defaultBlockState();


        BlockPos pos = new BlockPos(origin).atY(1);
        while (pos.getY() < world.getHeight() && !(world.getBlockState(pos).is(ModTags.Blocks.END_PLANTS_GROWABLE_ON)
                && world.getBlockState(pos.above()).is(Blocks.AIR))) {

            pos = pos.above();
        }
        pos = pos.above();

        for (int i = 0; i < size; i++) {
            if (pos.getY() >= world.getMaxBuildHeight() - 1)
                return false;

            world.setBlock(pos, top.setValue(CrystalShardBlock.IS_TIP, i == size - 1), 0x10);
            pos = pos.above();
        }

        pos = new BlockPos(origin).atY(0);
        while (pos.getY() < world.getHeight() && !(world.getBlockState(pos.above()).is(ModTags.Blocks.END_PLANTS_GROWABLE_ON)
                && world.getBlockState(pos).is(Blocks.AIR))) {

            pos = pos.above();
        }

        for (int i = 0; i < size; i++) {
            if (pos.getY() <= world.getMinBuildHeight() + 1 || pos.getY() >= 248)
                return false;

            world.setBlock(pos, bottom.setValue(CrystalShardBlock.IS_UP,false).setValue(CrystalShardBlock.IS_TIP, i == size - 1), 0x10);
            pos = pos.below();
        }

        return true;
    }
}