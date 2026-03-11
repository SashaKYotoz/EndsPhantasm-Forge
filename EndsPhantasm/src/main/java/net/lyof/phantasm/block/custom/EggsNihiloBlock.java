package net.lyof.phantasm.block.custom;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EggsNihiloBlock extends Block {
    public static final IntegerProperty SERVINGS = IntegerProperty.create("servings", 0, 4);

    protected static final VoxelShape SHAPE =
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 9.0D, 13.0D);

    public EggsNihiloBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(SERVINGS, 4));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SERVINGS);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return state.getValue(SERVINGS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canSurvive(world, pos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).isSolid();
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide()) {
            if (this.tryEat(world, pos, state, player).consumesAction())
                return InteractionResult.SUCCESS;

            if (player.getItemInHand(hand).isEmpty())
                return InteractionResult.CONSUME;
        }
        return this.tryEat(world, pos, state, player);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        int i = state.getValue(SERVINGS);
        if (i < 4 && random.nextDouble() < 0.05)
            world.setBlock(pos, state.setValue(SERVINGS, i + 1), Block.UPDATE_ALL);
    }

    public InteractionResult tryEat(Level world, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(false) || state.getValue(SERVINGS) == 0)
            return InteractionResult.PASS;

        player.eat(world, this.asItem().getDefaultInstance());
        world.addDestroyBlockEffect(pos, state);
        world.setBlock(pos, state.setValue(SERVINGS, state.getValue(SERVINGS) - 1), 3);

        return InteractionResult.SUCCESS;
    }
}
