package net.lyof.phantasm.entity.custom;

import com.google.gson.JsonObject;
import io.netty.buffer.Unpooled;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.entity.goals.FindBandGoal;
import net.lyof.phantasm.setup.ModPackets;
import net.lyof.phantasm.setup.packets.PolyppieServerUpdatePacket;
import net.lyof.phantasm.setup.packets.PolyppieStartsBeingCarriedPacket;
import net.lyof.phantasm.setup.packets.PolyppieStopsBeingCarriedPacket;
import net.lyof.phantasm.sound.SongHandler;
import net.lyof.phantasm.sound.custom.PolyppieSoundInstance;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class PolyppieEntity extends TamableAnimal implements VariantHolder<PolyppieEntity.Variant> {
    private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(PolyppieEntity.class,
            EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<ResourceLocation> VARIANT = SynchedEntityData.defineId(PolyppieEntity.class,
            ModEntities.TRACKED_LOCATION.get());
    private static final EntityDataAccessor<Boolean> PAUSED = SynchedEntityData.defineId(PolyppieEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PLAYING = SynchedEntityData.defineId(PolyppieEntity.class,
            EntityDataSerializers.BOOLEAN);

    public long tickCount;
    public long recordStartTick;
    protected int ticksThisSecond;

    protected int soundKey;
    protected Band band = null;

    public PolyppieEntity(EntityType<? extends TamableAnimal> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob entity) {
        return ModEntities.POLYPPIE.get().create(level);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(Items.AMETHYST_SHARD), false));
        this.goalSelector.addGoal(4, new FindBandGoal(this, 1));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6));
        Goal look = new RandomLookAroundGoal(this);
        look.setFlags(EnumSet.of(Goal.Flag.LOOK));
        this.goalSelector.addGoal(7, look);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16)
                .add(Attributes.MOVEMENT_SPEED, 0.15)
                .add(Attributes.ARMOR, 4);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(ITEM_STACK, ItemStack.EMPTY);
        this.getEntityData().define(VARIANT, Variant.DEFAULT_ID);
        this.getEntityData().define(PAUSED, false);
        this.getEntityData().define(PLAYING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        if (!this.getStack().isEmpty()) {
            nbt.put("RecordItem", this.getStack().save(new CompoundTag()));
        }

        nbt.putBoolean("IsPlaying", this.isPlayingRecord());
        nbt.putLong("TickCount", this.tickCount - this.recordStartTick);

        nbt.putInt("SoundKey", this.getSoundKey());
        nbt.putString("Variant", this.getVariant().id.toString());
        nbt.putBoolean("Paused", this.isPaused());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("RecordItem", 10))
            this.setStack(ItemStack.of(nbt.getCompound("RecordItem")));

        this.setPlaying(nbt.getBoolean("IsPlaying"));
        this.recordStartTick = 0;
        this.tickCount = nbt.getLong("TickCount");

        this.setSoundKey(nbt.getInt("SoundKey"));
        this.setVariant(Variant.get(ResourceLocation.parse(nbt.getString("Variant"))));
        this.setPaused(nbt.getBoolean("Paused"));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.getSoundKey());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.setSoundKey(packet.getData());

        if (this.level().isClientSide()) {
            PolyppieSoundInstance soundInstance = SongHandler.instance.get(this.getSoundKey());
            if (soundInstance != null) soundInstance.update(this);
        }
    }

    @Override
    public float getSpeed() {
        return this.getBand().canMove() ? super.getSpeed() : 0;
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.6;
    }

    @Override
    protected void updateControlFlags() {
        super.updateControlFlags();
        this.goalSelector.setControlFlag(Goal.Flag.LOOK, true);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.25f;
    }

    public ItemStack getStack() {
        return this.getEntityData().get(ITEM_STACK);
    }

    public void setStack(ItemStack stack) {
        this.getEntityData().set(ITEM_STACK, stack);
    }

    public void initSoundKey() {
        if (this.getSoundKey() <= 0)
            this.setSoundKey((int) (System.currentTimeMillis() % 10000));
    }

    public int getSoundKey() {
        return this.soundKey;
    }

    public void setSoundKey(int soundKey) {
        this.soundKey = soundKey;
    }

    @Override
    public Variant getVariant() {
        return Variant.get(this.getEntityData().get(VARIANT));
    }

    @Override
    public void setVariant(Variant variant) {
        this.getEntityData().set(VARIANT, variant.id);
    }

    public boolean isPaused() {
        return this.getEntityData().get(PAUSED);
    }

    public void setPaused(boolean paused) {
        this.getEntityData().set(PAUSED, paused);
        if (this.isPaused()) this.stopPlaying();
    }

    public void togglePaused() {
        this.setPaused(!this.isPaused());
    }

    public void setPlaying(boolean playing) {
        this.getEntityData().set(PLAYING, playing);
    }

    public Band getBand() {
        if (this.band == null)
            this.band = new Band(this);
        return this.band;
    }

    public boolean isValidDisc(ItemStack stack) {
        return stack.is(ItemTags.MUSIC_DISCS);
    }

    public boolean isPlayingRecord() {
        return !this.getStack().isEmpty() && this.getEntityData().get(PLAYING);
    }

    protected void sendSongPacket(boolean start) {
        if (this.level() instanceof ServerLevel ServerLevel) {
            this.initSoundKey();

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeNbt(start ? this.getStack().save(new CompoundTag()) : new CompoundTag());
            buf.writeInt(this.isCarried() ? this.getOwner().getId() : this.getId());
            buf.writeInt(this.getSoundKey());

            for (ServerPlayer player : ServerLevel.getServer().getPlayerList().getPlayers())
                ModPackets.sendToPlayer(new PolyppieServerUpdatePacket(buf), player);
        }
    }

    public void startPlaying() {
        this.recordStartTick = this.tickCount;
        this.setPlaying(true);
        this.level().gameEvent(GameEvent.JUKEBOX_PLAY, this.getOnPos(), GameEvent.Context.of(this));

        this.sendSongPacket(true);
    }

    public void stopPlaying() {
        this.setPlaying(false);
        this.level().gameEvent(GameEvent.JUKEBOX_STOP_PLAY, this.getOnPos(), GameEvent.Context.of(this));

        this.sendSongPacket(false);
    }

    public boolean isSongFinished() {
//        if (Phantasm.isVinURLLoaded() && VinURLCompat.isVinURLDisc(this.getStack())) {
//            CompoundTag nbt = getStack().getOrCreateNbt();
//            return this.tickCount > this.recordStartTick + nbt.getInt("duration") * 20L;
//        }
        return this.tickCount >= this.recordStartTick + ((RecordItem) getStack().getItem()).getLengthInTicks() + 20L;
    }

    public float getSongProgress() {
        if (!(this.getStack().getItem() instanceof RecordItem disc)) return 0;
        if (!this.isPlayingRecord()) return 0;

//        if (Phantasm.isVinURLLoaded() && VinURLCompat.isVinURLDisc(this.getStack())) {
//            CompoundTag nbt = getStack().getOrCreateNbt();
//            return (this.tickCount - this.recordStartTick) / (nbt.getInt("duration") * 20f);
//        }
        return (this.tickCount - this.recordStartTick) / (float) (disc.getLengthInTicks() + 20L);
    }

    public void spawnNoteParticle() {
        if (this.level() instanceof ServerLevel level) {
            Vec3 vec3d = this.getPosition(0).add(0.0, 1.2, 0.0);
            level.sendParticles(ParticleTypes.NOTE, vec3d.x(), vec3d.y(), vec3d.z(), 0,
                    level.getRandom().nextInt(4) / 24.0f, 0.0, 0.0, 1.0);
        }
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public void unsetRemoved() {
        super.unsetRemoved();
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        this.stopPlaying();
        this.spawnAtLocation(this.getStack());
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.isEmpty() && this.canBeCarriedBy(player) && player.isCrouching()) {
            this.setCarriedBy(player, null);
            return InteractionResult.sidedSuccess(player.level().isClientSide());
        }

        Variant variant = Variant.get(stack);
        if (variant != null && variant != this.getVariant()) {
            if (!this.level().isClientSide())
                this.setVariant(variant);

            stack.shrink(1);
            this.level().playSound(player, this.getOnPos(), SoundEvents.CORAL_BLOCK_PLACE, SoundSource.PLAYERS);
            return InteractionResult.sidedSuccess(player.level().isClientSide());
        }

        if (this.isValidDisc(stack) && this.getStack().isEmpty()) {
            this.setStack(stack.copyWithCount(1));
            stack.shrink(1);
            return InteractionResult.sidedSuccess(player.level().isClientSide());

        }

        if (!this.getStack().isEmpty()) {
            this.stopPlaying();
            this.spawnAtLocation(this.getStack());
            this.setStack(ItemStack.EMPTY);
            return InteractionResult.sidedSuccess(player.level().isClientSide());
        }
        return InteractionResult.PASS;
    }

    public void joinBand(PolyppieEntity target) {
        while (target.getFirstPassenger() instanceof PolyppieEntity it)
            target = it;
        if (!target.isVehicle() && target != this) {
            this.startRiding(target);
        }
    }

    public boolean isCarried() {
        return this.getRemovalReason() == RemovalReason.UNLOADED_WITH_PLAYER && this.getOwner() != null;
    }

    public boolean canBeCarriedBy(Player player) {
        return player instanceof PolyppieCarrier carrier && carrier.getCarriedPolyppie() == null;
    }

    public void setCarriedBy(Player player, Vec3 position) {
        if (position == null) {
            if (player.level() instanceof ServerLevel ServerLevel) {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                buf.writeInt(this.getId());
                buf.writeInt(player.getId());
                for (ServerPlayer p : ServerLevel.getServer().getPlayerList().getPlayers())
                    ModPackets.sendToPlayer(new PolyppieStartsBeingCarriedPacket(buf), p);
            }

            this.removeVehicle();
            this.getPassengers().forEach(Entity::removeVehicle);

            this.remove(RemovalReason.UNLOADED_WITH_PLAYER);
            this.tame(player);
            ((PolyppieCarrier) player).setCarriedPolyppie(this);
        } else {
            this.setPos(position);
            this.setRot(180 + player.getYHeadRot(), 0);
            ((PolyppieCarrier) player).setCarriedPolyppie(null);

            if (player.level() instanceof ServerLevel ServerLevel) {
                this.setTame(false);
                this.setOwnerUUID(null);
                this.unsetRemoved();

                PolyppieEntity polyppie = ModEntities.POLYPPIE.get().create(this.level());
                polyppie.load(this.saveWithoutId(new CompoundTag()));
                this.level().addFreshEntity(polyppie);

                this.discard();

                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                buf.writeInt(polyppie.getId());
                buf.writeInt(player.getId());
                buf.writeDouble(position.x);
                buf.writeDouble(position.y);
                buf.writeDouble(position.z);
                for (ServerPlayer p : ServerLevel.getServer().getPlayerList().getPlayers())
                    ModPackets.sendToPlayer(new PolyppieStopsBeingCarriedPacket(buf), p);

                polyppie.setVariant(this.getVariant());
            }
        }
    }

    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isPlayingRecord() && this.getStack().getItem() instanceof RecordItem) {
            this.ticksThisSecond++;

            if (this.isSongFinished()) {
                this.stopPlaying();
            } else if (this.ticksThisSecond >= 20) {
                this.ticksThisSecond = 0;
                this.level().gameEvent(GameEvent.JUKEBOX_PLAY, this.getOnPos(), GameEvent.Context.of(this));
                this.spawnNoteParticle();
            }
        } else if (this.tickCount % 20 == 0 && !this.isPassenger()) {
            this.band = new Band(this);
            if (this.band.getPlaying() == null && !this.level().isClientSide())
                this.band.startRandom(this.getRandom());
        }

        this.tickCount++;
    }


    public static class Band {
        protected List<PolyppieEntity> members;

        public Band(PolyppieEntity base) {
            this.members = base.getRootVehicle().getPassengersAndSelf().filter(e -> e instanceof PolyppieEntity)
                    .map(e -> (PolyppieEntity) e).toList();
            this.members.forEach(e -> e.band = this);
        }

        public boolean contains(int id) {
            for (PolyppieEntity polyppie : this.members) {
                if (polyppie.getId() == id) return true;
            }
            return false;
        }

        public void startRandom(RandomSource random) {
            List<PolyppieEntity> singers = new ArrayList<>();
            for (PolyppieEntity polyppie : this.members) {
                if (!polyppie.getStack().isEmpty() && !polyppie.isPaused())
                    singers.add(polyppie);
            }
            if (singers.isEmpty()) return;
            singers.get(random.nextInt(singers.size())).startPlaying();
        }

        public PolyppieEntity getPlaying() {
            for (PolyppieEntity polyppie : this.members) {
                if (polyppie.isPlayingRecord())
                    return polyppie;
            }
            return null;
        }

        public boolean canMove() {
            if (this.members.size() > 1) return false;
            for (PolyppieEntity polyppie : this.members) {
                if (!polyppie.getStack().isEmpty())
                    return false;
            }
            return true;
        }
    }


    public static class Variant {
        private static final ResourceLocation DEFAULT_ID = Phantasm.makeID("/default");
        private static Variant DEFAULT = null;
        private static final Map<ResourceLocation, Variant> instances = new HashMap<>();

        public final ResourceLocation id;
        public final Item coral;
        public final ResourceLocation texture;

        public static Variant getDefault() {
            if (DEFAULT == null)
                DEFAULT = new Variant(DEFAULT_ID, ModBlocks.CHORAL_BLOCK.get().asItem(), Phantasm.makeID("textures/entity/polyppie/polyppie.png"));
            return DEFAULT;
        }

        protected Variant(ResourceLocation id, Item coral, ResourceLocation texture) {
            this.id = id;
            this.coral = coral;
            this.texture = texture;
        }

        public static void read(ResourceLocation id, JsonObject json) {
            if (json.has("coral") && json.has("texture")) {
                Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(json.get("coral").getAsString()));
                if (item != Items.AIR)
                    instances.putIfAbsent(id, new Variant(id, item, ResourceLocation.parse(json.get("texture").getAsString())));
            }
        }

        public static void clear() {
            instances.clear();
        }

        public static Variant get(ResourceLocation id) {
            return instances.getOrDefault(id, getDefault());
        }

        public static Variant get(ItemStack stack) {
            if (stack.is(getDefault().coral))
                return DEFAULT;
            for (Map.Entry<ResourceLocation, Variant> entry : instances.entrySet()) {
                if (stack.is(entry.getValue().coral))
                    return entry.getValue();
            }
            return null;
        }

        public static void read(FriendlyByteBuf packet) {
            ResourceLocation id = packet.readResourceLocation();
            Item coral = ForgeRegistries.ITEMS.getValue(packet.readResourceLocation());
            ResourceLocation texture = packet.readResourceLocation();

            instances.putIfAbsent(id, new Variant(id, coral, texture));
        }

        public static void write(List<FriendlyByteBuf> packets) {
            for (Map.Entry<ResourceLocation, Variant> entry : instances.entrySet()) {
                FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
                packet.writeInt(1);

                packet.writeResourceLocation(entry.getKey());
                if (ForgeRegistries.ITEMS.getKey(entry.getValue().coral) != null)
                    packet.writeResourceLocation(ForgeRegistries.ITEMS.getKey(entry.getValue().coral));
                packet.writeResourceLocation(entry.getValue().texture);

                packets.add(packet);
            }
        }
    }
}