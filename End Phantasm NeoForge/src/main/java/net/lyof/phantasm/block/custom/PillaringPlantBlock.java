package net.lyof.phantasm.block.custom;

import com.mojang.serialization.MapCodec;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PillaringPlantBlock extends BushBlock implements BonemealableBlock {
    public TagKey<Block> growableOn;
    public VoxelShape shape;

    public PillaringPlantBlock(Properties properties, TagKey<Block> growableOn, VoxelShape shape) {
        super(properties);
        this.growableOn = growableOn;
        this.shape = shape;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return this.shape;
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return simpleCodec(properties -> new PillaringPlantBlock(properties, this.growableOn, this.shape));
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter getter, BlockPos pos) {
        return floor.is(this.growableOn);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader reader, BlockPos pos, BlockState state) {
        return reader.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextInt(3) == 0 && this.isValidBonemealTarget(level, pos, state);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (state.is(ModBlocks.DRALGAE.get()) && random.nextInt(10) == 0)
            level.setBlock(pos.above(), ModBlocks.POME.get().defaultBlockState(), 3);
        else
            level.setBlock(pos.above(), this.defaultBlockState(), 3);
    }
}