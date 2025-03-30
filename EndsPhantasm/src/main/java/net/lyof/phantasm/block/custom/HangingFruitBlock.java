package net.lyof.phantasm.block.custom;


import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HangingFruitBlock extends HangingPlantBlock implements BonemealableBlock {
    public static final BooleanProperty HAS_FRUIT = BooleanProperty.create("has_fruit");
    private final String getFruitID;

    public HangingFruitBlock(BlockBehaviour.Properties properties, TagKey<Block> growable_on, VoxelShape shape, String getFruitID) {
        super(properties.randomTicks(), growable_on, shape);
        this.registerDefaultState(this.defaultBlockState().setValue(HAS_FRUIT, false));
        this.growableOn = growable_on;
        this.getFruitID = getFruitID;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_FRUIT);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(HAS_FRUIT)) return;
        if (random.nextDouble() < 0.05 && !state.getValue(HAS_FRUIT))
            this.performBonemeal(level, random, pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (state.getValue(HAS_FRUIT)) {
            player.swing(hand);
            ItemStack fruitStack = ItemStack.EMPTY;
            switch (this.getFruitID) {
                case "pream_berry" -> fruitStack = new ItemStack(ModItems.PREAM_BERRY.get());
                case "oblifruit" -> fruitStack = new ItemStack(ModItems.OBLIFRUIT.get());
            }
            player.spawnAtLocation(fruitStack);
            level.setBlock(pos, state.setValue(HAS_FRUIT, false), 3);
        }
        return super.use(state, level, pos, player, hand, result);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader reader, BlockPos pos, BlockState state, boolean p_50900_) {
        return !state.getValue(HAS_FRUIT);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return canSurvive(state, level, pos);
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(HAS_FRUIT, true), 3);
        if (this.getFruitID.equals("oblifruit") && level.getBlockState(pos.below()).isAir() && random.nextFloat() > 0.875)
            level.setBlock(pos.below(), ModBlocks.CRYSTALILY.get().defaultBlockState(), 3);
    }
}
