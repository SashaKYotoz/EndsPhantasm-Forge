package net.lyof.phantasm.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;

public class DragonMintBlock extends DoublePlantBlock implements BonemealableBlock {
    public DragonMintBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(HangingFruitBlock.HAS_FRUIT, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HangingFruitBlock.HAS_FRUIT);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(HangingFruitBlock.HAS_FRUIT)) return;
        if (random.nextDouble() < 0.02 && this.isBonemealSuccess(level, random, pos, state))
            this.performBonemeal(level, random, pos, state);
    }


    public static BlockState getOtherHalf(LevelReader reader, BlockPos pos, BlockState state) {
        return state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER ?
                reader.getBlockState(pos.above()) : reader.getBlockState(pos.below());
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !state.getValue(HangingFruitBlock.HAS_FRUIT) && !getOtherHalf(level, pos, state).getValue(HangingFruitBlock.HAS_FRUIT);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return !state.getValue(HangingFruitBlock.HAS_FRUIT) && !getOtherHalf(level, pos, state).getValue(HangingFruitBlock.HAS_FRUIT);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (random.nextInt(3) == 0)
            level.setBlock(pos, state.setValue(HangingFruitBlock.HAS_FRUIT, true), 3);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack,BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.is(Items.GLASS_BOTTLE) && state.getValue(HangingFruitBlock.HAS_FRUIT)) {
            level.setBlock(pos, state.setValue(HangingFruitBlock.HAS_FRUIT, false), 3);

            if (!player.isCreative()) stack.shrink(1);
            player.getInventory().placeItemBackInInventory(Items.DRAGON_BREATH.getDefaultInstance());

            player.awardStat(Stats.ITEM_USED.get(Items.GLASS_BOTTLE));
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }
}