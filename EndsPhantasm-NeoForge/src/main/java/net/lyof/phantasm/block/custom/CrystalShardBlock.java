package net.lyof.phantasm.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrystalShardBlock extends Block implements SimpleWaterloggedBlock {
    protected static final VoxelShape SHAPE = box(2, 0, 2, 14, 16, 14);

    public static final BooleanProperty IS_UP = BooleanProperty.create("is_up");
    public static final BooleanProperty IS_TIP = BooleanProperty.create("is_tip");
    public static final BooleanProperty WATERLOGGED = BooleanProperty.create("waterlogged");

    public CrystalShardBlock(Properties properties) {
        super(properties.noOcclusion());
        this.registerDefaultState(this.defaultBlockState()
                .setValue(IS_TIP, true)
                .setValue(IS_UP, true)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(IS_TIP).add(IS_UP).add(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        return reader.getBlockState(pos.above()).isSolid() || reader.getBlockState(pos.above()).is(this.asBlock()) ||
                reader.getBlockState(pos.below()).isSolid() || reader.getBlockState(pos.below()).is(this.asBlock());
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos1, boolean b) {
        super.neighborChanged(state, level, pos, block, pos1, b);
        if (!canSurvive(state,level,pos))
            level.destroyBlock(pos,true);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = this.defaultBlockState()
                .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));

        boolean down = level.getBlockState(pos.above()).isFaceSturdy(level, pos, Direction.DOWN)
                || level.getBlockState(pos.above()).getBlock() == this.asBlock();
        boolean up = level.getBlockState(pos.below()).isFaceSturdy(level, pos, Direction.UP)
                || level.getBlockState(pos.below()).getBlock() == this.asBlock();

        if (down && up) {
            if (context.getNearestLookingVerticalDirection() == Direction.UP) return state.setValue(IS_UP, false);
        }
        else if (down)
            return state.setValue(IS_UP, false);

        return state;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor accessor,
                                  BlockPos pos, BlockPos neighborPos) {

        if (state.getValue(WATERLOGGED)) {
            accessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
        }

        if (isValidPos(accessor, neighborPos, direction.getOpposite())) {
            if (direction == getDirection(state.getValue(IS_UP)))
                return state.setValue(IS_TIP, false);
        }

        else if (direction == getDirection(state.getValue(IS_UP)).getOpposite()) {
            return Blocks.AIR.defaultBlockState();
        }

        if (direction == getDirection(state.getValue(IS_UP)))
            return state.setValue(IS_TIP, true);

        return state;
    }

    public boolean isValidPos(LevelAccessor accessor, BlockPos pos, Direction direction) {
        BlockState state = accessor.getBlockState(pos);
        return state.isFaceSturdy(accessor, pos, direction) || state.getBlock() == this.asBlock();
    }

    public static Direction getDirection(boolean up) {
        return up ? Direction.UP : Direction.DOWN;
    }
}
