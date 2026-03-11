package net.lyof.phantasm.block.entities;

import io.netty.buffer.Unpooled;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.challenge.Challenge;
import net.lyof.phantasm.block.challenge.ChallengeRegistry;
import net.lyof.phantasm.block.custom.ChallengeRuneBlock;
import net.lyof.phantasm.entity.access.Challenger;
import net.lyof.phantasm.setup.ModPackets;
import net.lyof.phantasm.setup.packets.ChallengeEndsPacket;
import net.lyof.phantasm.setup.packets.ChallengeStartsPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChallengeRuneBlockEntity extends BlockEntity {
    private static final ResourceLocation DRAGON = ResourceLocation.fromNamespaceAndPath("minecraft", "end/kill_dragon");

    private final List<UUID> completedPlayerUuids;
    private final List<UUID> challengerUuids;
    public int tick;
    private int progress;
    private final ServerBossEvent bossbar;
    public Challenge challenge = ChallengeRegistry.EMPTY;

    public boolean renderBase;

    public ChallengeRuneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHALLENGE_RUNE.get(), pos, state);
        this.completedPlayerUuids = new ArrayList<>();
        this.challengerUuids = new ArrayList<>();
        this.bossbar = new ServerBossEvent(ModBlocks.CHALLENGE_RUNE.get().getName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.NOTCHED_20);

        this.tick = -1;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        this.setChallenge(ResourceLocation.parse(nbt.getString("ChallengeId")));

        int size = nbt.getInt("CompletedPlayerCount");
        for (int i = 0; i < size; i++)
            this.completedPlayerUuids.add(nbt.getUUID("Player" + i));

        size = nbt.getInt("ChallengerCount");
        for (int i = 0; i < size; i++)
            this.challengerUuids.add(nbt.getUUID("Challenger" + i));

        this.renderBase = nbt.getBoolean("RenderBase");
        this.tick = nbt.getInt("ChallengeTick");
        this.progress = nbt.getInt("Progress");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        nbt.putString("ChallengeId", this.challenge.id.toString());

        nbt.putInt("CompletedPlayerCount", this.completedPlayerUuids.size());
        for (int i = 0; i < this.completedPlayerUuids.size(); i++)
            nbt.putUUID("Player" + i, this.completedPlayerUuids.get(i));

        nbt.putInt("ChallengerCount", this.challengerUuids.size());
        for (int i = 0; i < this.challengerUuids.size(); i++)
            nbt.putUUID("Challenger" + i, this.challengerUuids.get(i));

        nbt.putBoolean("RenderBase", this.renderBase);
        nbt.putInt("ChallengeTick", this.tick);
        nbt.putInt("Progress", this.progress);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        this.saveAdditional(nbt);
        return nbt;
    }


    public void setChallenge(ResourceLocation id) {
        this.challenge = ChallengeRegistry.get(id);
        this.bossbar.setOverlay(BossEvent.BossBarOverlay.byName("notched_" + this.challenge.monsterObjective));
    }

    public void complete(Player player) {
        if (!this.hasCompleted(player))
            this.completedPlayerUuids.add(player.getUUID());

        if (player instanceof ServerPlayer serverPlayer)
            CriteriaTriggers.GENERATE_LOOT.trigger(serverPlayer, this.challenge.lootTable);
    }

    public boolean hasCompleted(Player player) {
        return this.completedPlayerUuids.contains(player.getUUID());
    }

    public void addChallenger(Player player) {
        this.challengerUuids.add(player.getUUID());
        ((Challenger) player).setChallengeRune(this);
    }

    public ChallengeRuneBlock.Condition getStartingCondition(ServerPlayer player) {
        if (this.challenge.monsterObjective <= 0) return ChallengeRuneBlock.Condition.EMPTY;
        ChallengeRuneBlockEntity rune = ((Challenger) player).getChallengeRune();
        if (this.isChallengeRunning() || (rune != null && !rune.getBlockPos().equals(this.getBlockPos()) && rune.isChallengeRunning()))
            return ChallengeRuneBlock.Condition.RUNNING;
        if (this.hasCompleted(player)) return ChallengeRuneBlock.Condition.COMPLETED;
        if (player.experienceLevel < this.challenge.levelCost) return ChallengeRuneBlock.Condition.EXPERIENCE;
        Advancement advc = player.getServer().getAdvancements().getAdvancement(DRAGON);
        if (advc != null && this.challenge.postDragon && !player.getAdvancements().getOrStartProgress(advc).isDone())
            return ChallengeRuneBlock.Condition.DRAGON;
        return ChallengeRuneBlock.Condition.NONE;
    }

    public boolean canStart(ServerPlayer player) {
        return getStartingCondition(player) == ChallengeRuneBlock.Condition.NONE;
    }

    public void displayHint(ChallengeRuneBlock.Condition reason, ServerPlayer player) {
        String message = "";
        if (this.isChallengeRunning() || reason == ChallengeRuneBlock.Condition.RUNNING)
            message = "";
        else if (this.hasCompleted(player) || reason == ChallengeRuneBlock.Condition.COMPLETED)
            message = ".completed" + level.random.nextInt(5);
        else if (reason == ChallengeRuneBlock.Condition.CRYSTAL)
            message = ".crystal" + level.random.nextInt(5);
        else if (reason == ChallengeRuneBlock.Condition.DRAGON)
            message = ".dragon" + level.random.nextInt(5);
        else if (reason == ChallengeRuneBlock.Condition.EXPERIENCE)
            message = ".experience" + level.random.nextInt(5);

        player.displayClientMessage(Component.translatable("block.phantasm.challenge_rune.hint" + message).withStyle(ChatFormatting.LIGHT_PURPLE),
                true);
    }

    public void progress() {
        this.progress++;
        this.bossbar.setProgress(1 - (float) this.progress / this.challenge.monsterObjective);

        if (this.progress >= this.challenge.monsterObjective)
            this.stopChallenge(true);
    }

    public void startChallenge(Player player) {
        this.startChallenge();

        if (!player.isCreative())
            player.giveExperienceLevels(-this.challenge.levelCost);

        for (Player participant : player.level().players()) {
            if (participant.position().distanceTo(Vec3.atLowerCornerOf(this.getBlockPos())) < Challenge.R) {
                if (!this.getLevel().isClientSide()) {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeBlockPos(this.getBlockPos());
                    ModPackets.sendToPlayer(new ChallengeStartsPacket(buf), (ServerPlayer) participant);
                }

                this.addChallenger(participant);
            }
        }
    }

    public void startChallenge() {
        this.tick = 0;
        this.progress = 0;
        this.challengerUuids.clear();

        this.level.playSound(null, this.getBlockPos(), SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS,
                10, 1);
        this.bossbar.setProgress(1);

        setChanged(level, worldPosition, this.getBlockState());
    }

    public void stopChallenge(boolean success) {
        this.tick = -2;
        this.progress = 0;

        for (UUID uuid : List.copyOf(this.challengerUuids)) {
            Challenger challenger = Challenger.get(uuid, this.getLevel());
            if (challenger == null) continue;

            if (success && challenger.getChallengeRune() == this && challenger.isInRange() && challenger.asPlayer().isAlive())
                this.complete(challenger.asPlayer());
            if (challenger.getChallengeRune() == this)
                challenger.setChallengeRune(null);
        }

        if (!this.getLevel().isClientSide()) {
            if (success)
                this.challenge.spawnLoot(this);

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeBlockPos(this.getBlockPos());
            buf.writeBoolean(success);

            for (Player player : this.getLevel().players()) {
                ModPackets.sendToPlayer(new ChallengeEndsPacket(buf), (ServerPlayer) player);
                this.bossbar.removePlayer((ServerPlayer) player);
            }
        }

        for (Entity entity : level.getEntitiesOfClass(Entity.class, AABB.of(new BoundingBox(this.getBlockPos().above((int) Challenge.R / 2))).inflate(Challenge.R * 2),
                e -> e instanceof Challenger challenger && challenger.getChallengeRune() == this)) {

            if (!(entity instanceof Player)) {
                entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
            }
        }

        this.challengerUuids.clear();
        this.getLevel().getChunkSource().updateChunkForced(this.getLevel().getChunk(this.getBlockPos()).getPos(), false);
        setChanged(this.getLevel(), this.getBlockPos(), this.getBlockState());
    }

    public boolean isChallengeRunning() {
        return this.tick >= 0;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ChallengeRuneBlockEntity self) {
        if (!self.isChallengeRunning() || level == null) return;

        if (self.tick % 20 == 0) {
            for (UUID uuid : List.copyOf(self.challengerUuids)) {
                Challenger challenger = Challenger.get(uuid, level);
                if (challenger == null) {
                    self.challengerUuids.remove(uuid);
                    continue;
                }

                if (!level.isClientSide()) {
                    self.bossbar.addPlayer((ServerPlayer) challenger.asPlayer());
                    self.bossbar.setProgress(1 - (float) self.progress / self.challenge.monsterObjective);
                }

                if (challenger.getChallengeRune() == null)
                    challenger.setChallengeRune(self);
                if (!challenger.isInRange() || !challenger.asPlayer().isAlive()) {
                    self.challengerUuids.remove(uuid);

                    if (!level.isClientSide())
                        self.bossbar.removePlayer((ServerPlayer) challenger.asPlayer());
                }
            }

            if (self.challengerUuids.isEmpty())
                self.stopChallenge(false);

            setChanged(level, pos, state);
        }

        if (self.tick == 100) {
            level.getEntitiesOfClass(Entity.class, AABB.of(new BoundingBox(pos)), e -> e instanceof EndCrystal)
                    .stream().findFirst().ifPresent(Entity::discard);
            if (level.isClientSide()) level.explode(null, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5,
                    2, Level.ExplosionInteraction.NONE);
            level.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
        }

        if (!level.isClientSide() && self.tick > 100 && self.tick <= 100 + 20 * self.challenge.monsterObjective
                && self.tick % 20 == 0) {
            self.challenge.spawnMonster(self);
        }

        self.tick++;
    }
}