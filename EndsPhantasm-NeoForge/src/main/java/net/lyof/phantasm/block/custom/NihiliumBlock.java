package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.world.ModConfiguredFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.neoforged.neoforge.event.level.BlockGrowFeatureEvent;

public class NihiliumBlock extends Block implements BonemealableBlock {
    public NihiliumBlock(Properties behaviour) {
        super(behaviour);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        int i = reader.getMaxLightLevel();
        return i > 4 || reader.getBlockState(pos.below()).is(Blocks.END_STONE);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
        if (!canSurvive(state, level, pos))
            level.setBlock(pos, Blocks.END_STONE.defaultBlockState(), 3);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return levelReader.getBlockState(blockPos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource source, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos target;
        BlockState block;

        for (int i = 0; i < 6; i++) {
            target = pos.mutable().move(random.nextIntBetweenInclusive(-1, 1), random.nextIntBetweenInclusive(-1, 1), random.nextIntBetweenInclusive(-1, 1));

            if (canSurvive(level.getBlockState(target), level, target) && level.getBlockState(target).getBlock() == Blocks.END_STONE) {
                level.setBlock(target, this.defaultBlockState(), 3);

                block = level.getBlockState(target);
                if (random.nextInt(3) == 0)
                    ((BonemealableBlock) block.getBlock()).performBonemeal(level, random, target, block);
            }
        }
        Holder<ConfiguredFeature<?, ?>> holder = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolder(ModConfiguredFeatures.VIVID_NIHILIUM).orElse(null);
        var event = new BlockGrowFeatureEvent(level, random, pos, holder);
        holder = event.getFeature();
        if (holder != null) {
            ConfiguredFeature<?, ?> configuredfeature = holder.value();
            BlockState blockstate = level.getFluidState(pos).createLegacyBlock();
            level.setBlock(pos, blockstate, 4);
            if (configuredfeature.place(level, level.getChunkSource().getGenerator(), random, pos)) {
                if (level.getBlockState(pos) == blockstate) {
                    level.sendBlockUpdated(pos, state, blockstate, 2);
                }

            }
        }
    }
}
