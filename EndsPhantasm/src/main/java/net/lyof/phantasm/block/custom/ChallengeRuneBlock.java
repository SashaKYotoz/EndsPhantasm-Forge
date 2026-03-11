package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.block.entities.ChallengeRuneBlockEntity;
import net.lyof.phantasm.block.entities.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ChallengeRuneBlock extends BaseEntityBlock {
    public static final BooleanProperty COMPLETED = BooleanProperty.create("completed");

    public ChallengeRuneBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(COMPLETED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(COMPLETED);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChallengeRuneBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public InteractionResult use(BlockState state, Level Level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.getItemInHand(hand).is(Items.END_CRYSTAL)) {
            if (!Level.isClientSide()) {
                if (Level.getBlockEntity(pos) instanceof ChallengeRuneBlockEntity challengeRune)
                    challengeRune.displayHint(Condition.CRYSTAL, (ServerPlayer) player);
            }
            return InteractionResult.sidedSuccess(Level.isClientSide());
        }

        return super.use(state, Level, pos, player, hand, hit);
    }

    @Override
    public void destroy(LevelAccessor Level, BlockPos pos, BlockState state) {
        if (Level.getBlockEntity(pos) instanceof ChallengeRuneBlockEntity challengeRune && challengeRune.isChallengeRunning())
            challengeRune.stopChallenge(false);
        super.destroy(Level, pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level Level, BlockPos pos, BlockState newState, boolean moved) {
        if (Level.getBlockEntity(pos) instanceof ChallengeRuneBlockEntity challengeRune && challengeRune.isChallengeRunning())
            challengeRune.stopChallenge(false);
        super.onRemove(state, Level, pos, newState, moved);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level Level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.CHALLENGE_RUNE.get(), ChallengeRuneBlockEntity::tick);
    }


    public enum Condition {
        CRYSTAL("crystal"),
        DRAGON("dragon"),
        EXPERIENCE("experience"),

        EMPTY("empty"),
        COMPLETED("completed"),
        RUNNING("running"),

        NONE("none");

        public final String name;

        Condition(String name) {
            this.name = name;
        }
    }
}
