package net.lyof.phantasm.block.custom;


import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HangingPlantBlock extends Block {
    public TagKey<Block> growableOn;
    public VoxelShape shape;

    public HangingPlantBlock(Properties settings, TagKey<Block> growable_on, VoxelShape shape) {
        super(settings);
        this.growableOn = growable_on;
        this.shape = shape;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return this.shape;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        return reader.getBlockState(pos.above()).is(this.growableOn);
    }
}
