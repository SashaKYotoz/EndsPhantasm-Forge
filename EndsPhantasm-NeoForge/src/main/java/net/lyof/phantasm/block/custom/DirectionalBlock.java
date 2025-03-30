package net.lyof.phantasm.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DirectionalBlock extends Block {
    public static final DirectionProperty FACING = DirectionProperty.create("facing");
    public static final Map<Direction, VoxelShape> SHAPES = Map.of(
            Direction.DOWN, Block.box(3, 0, 3, 13, 10, 13),
            Direction.UP, Block.box(3, 6, 3, 13, 16, 13),
            Direction.NORTH, Block.box(3, 3, 0, 13, 13, 10),
            Direction.SOUTH, Block.box(3, 3, 6, 13, 13, 16),
            Direction.EAST, Block.box(6, 3, 3, 16, 13, 13),
            Direction.WEST, Block.box(0, 3, 3, 10, 13, 13)
    );

    public DirectionalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.DOWN));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        BlockPos blockPos = pos.relative(state.getValue(FACING));
        BlockState support = reader.getBlockState(blockPos);
        return support.isFaceSturdy(reader, blockPos, state.getValue(FACING));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState other, LevelAccessor accessor, BlockPos pos, BlockPos otherPos) {
        BlockState result = super.updateShape(state, direction, other, accessor, pos, otherPos);
        if (!result.is(this)) return result;

        return this.canSurvive(state, accessor, pos) ? result : Blocks.AIR.defaultBlockState();
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    public BlockState getPlacementState(LevelReader world, BlockPos pos) {
        List<Direction> possible = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            if (this.canSurvive(this.defaultBlockState().setValue(FACING, dir), world, pos))
                possible.add(dir);
        }
        Collections.shuffle(possible);
        if (possible.isEmpty()) return Blocks.AIR.defaultBlockState();
        return this.defaultBlockState().setValue(FACING, possible.get(0));
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(20) == 0) {
            world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1, 1, true);
        }
        super.animateTick(state, world, pos, random);
    }
}
