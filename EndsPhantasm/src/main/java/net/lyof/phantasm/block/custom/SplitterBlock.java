package net.lyof.phantasm.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

public class SplitterBlock extends DiodeBlock {
    public static DirectionProperty OUTPUT = DirectionProperty.create("output", Direction.Plane.HORIZONTAL);
    public static BooleanProperty MODE2 = BooleanProperty.create("mode");

    public SplitterBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(POWERED, false)
                .setValue(OUTPUT, Direction.NORTH).setValue(MODE2, false));
    }

    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, OUTPUT, MODE2);
    }

    @Override
    protected int getDelay(BlockState state) {
        return 2;
    }

    protected Direction chooseOutput(RandomSource random, BlockState state) {
        Direction dir = state.getValue(FACING);
        if (state.getValue(MODE2) || random.nextInt(3) != 0) {
            if (random.nextBoolean()) dir = dir.getClockWise();
            else dir = dir.getCounterClockWise();
        }
        return dir;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.getAbilities().mayBuild) {
            return InteractionResult.PASS;
        } else {
            level.setBlock(pos, state.setValue(MODE2, !state.getValue(MODE2)), 3);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    @Override
    public int getSignal(BlockState state, BlockGetter Level, BlockPos pos, Direction direction) {
        return super.getSignal(state, Level, pos, direction == state.getValue(OUTPUT) ? state.getValue(FACING) : state.getValue(FACING).getOpposite());
    }

    @Override
    protected void checkTickOnNeighbor(Level Level, BlockPos pos, BlockState state) {
        boolean had = state.getValue(POWERED);
        boolean has = this.shouldTurnOn(Level, pos, state);
        if (has && !had)
            Level.setBlock(pos, state.setValue(OUTPUT, this.chooseOutput(Level.random, state)), Block.UPDATE_CLIENTS);

        super.checkTickOnNeighbor(Level, pos, state);
    }

    @Override
    protected void updateNeighborsInFront(Level Level, BlockPos pos, BlockState state) {
        super.updateNeighborsInFront(Level, pos, state.setValue(FACING, state.getValue(OUTPUT)));
    }
}