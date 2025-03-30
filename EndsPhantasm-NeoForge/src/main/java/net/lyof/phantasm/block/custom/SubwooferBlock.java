package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SubwooferBlock extends Block {
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    public static final DirectionProperty FACING = DirectionProperty.create("facing");

    public SubwooferBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false).setValue(FACING, Direction.NORTH));
    }

    public static boolean canPush(Entity e) {
        return e.isPushable() || e instanceof ItemEntity || e instanceof Projectile;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx).setValue(FACING, ctx.getPlayer() != null && ctx.getPlayer().isShiftKeyDown() ?
                ctx.getNearestLookingDirection().getOpposite() : ctx.getNearestLookingDirection());
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighbor, boolean flag) {
        boolean b = level.hasNeighborSignal(pos);
        Direction dir = state.getValue(FACING);
        BlockPos p;
        if (b != state.getValue(POWERED)) {
            level.setBlock(pos, state.setValue(POWERED, b), 3);
            if (b) {
                List<UUID> affected = new ArrayList<>();

                for (int i = 1; i < ConfigEntries.subwooferRange; i++) {
                    p = pos.mutable().relative(dir, i);
                    if (level.getBlockState(p).is(BlockTags.OCCLUDES_VIBRATION_SIGNALS)) return;

                    level.blockEvent(pos, this, dir.get3DDataValue(), i);
                    level.gameEvent(null, GameEvent.NOTE_BLOCK_PLAY, pos);

                    List<Entity> entities = level.getEntities((Entity) null, new AABB(p).inflate(1.2), SubwooferBlock::canPush);
                    for (Entity e : entities) {
                        if (affected.contains(e.getUUID())) continue;

                        affected.add(e.getUUID());
                        e.setDeltaMovement(new Vec3(dir.getStepX(), dir.getStepY() + 0.1, dir.getStepZ()));
                        e.hurtMarked = true;
                        if (dir == Direction.UP) e.fallDistance = 0;
                    }
                }
            }
        }
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int type, int data) {
        if (data == 1 && !level.getBlockState(pos.offset(state.getValue(FACING).getOpposite().getNormal())).is(BlockTags.OCCLUDES_VIBRATION_SIGNALS))
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.BLOCKS,
                    0.2f, 1.5f, true);

        BlockPos p = pos.mutable().relative(Direction.from3DDataValue(type), data);
        level.addAlwaysVisibleParticle(ParticleTypes.SONIC_BOOM, p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5,
                0, 0, 0);
        return true;
    }
}