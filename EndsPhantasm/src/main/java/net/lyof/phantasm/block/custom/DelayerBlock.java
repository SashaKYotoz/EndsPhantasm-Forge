package net.lyof.phantasm.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class DelayerBlock extends DiodeBlock {
    public static BooleanProperty DELAYING = BooleanProperty.create("delaying");
    private int receivedPower = 0;

    public DelayerBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false).setValue(FACING, Direction.NORTH)
                .setValue(DELAYING, false));
    }
    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, DELAYING);
    }

    @Override
    protected int getDelay(BlockState state) {
        return this.receivedPower * 20;
    }

    protected void updateReceivedPower(Level Level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        Direction right = facing.getClockWise();
        Direction left = facing.getCounterClockWise();
        boolean gatesOnly = this.sideInputDiodesOnly();
        this.receivedPower = Level.getControlInputSignal(pos.relative(right), right, gatesOnly)
                + Level.getControlInputSignal(pos.relative(left), left, gatesOnly);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        this.updateReceivedPower(level, pos, state);
        player.displayClientMessage(Component.translatable("block.phantasm.delayer.display", this.receivedPower), true);
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected void checkTickOnNeighbor(Level Level, BlockPos pos, BlockState state) {
        this.updateReceivedPower(Level, pos, state);

        boolean had = state.getValue(POWERED);
        boolean has = this.shouldTurnOn(Level, pos, state);
        if (had != has) Level.setBlock(pos, state.setValue(DELAYING, has), Block.UPDATE_CLIENTS);

        if (state.getValue(DELAYING) && has == had)
            return;

        super.checkTickOnNeighbor(Level, pos, Level.getBlockState(pos));
    }
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        boolean had = state.getValue(POWERED);
        boolean has = this.shouldTurnOn(level, pos, state);
        if (had != has) level.setBlock(pos, state.setValue(DELAYING, has), Block.UPDATE_CLIENTS);

        state = level.getBlockState(pos);

        int save = this.receivedPower;
        this.receivedPower = 1;
        super.tick(state, level, pos, random);
        this.receivedPower = save;
    }
}