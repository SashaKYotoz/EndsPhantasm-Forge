package net.lyof.phantasm.mixin;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.access.Challenger;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Challenger, PolyppieCarrier {
    public PlayerEntityMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(at = @At("RETURN"), method = "getDigSpeed", cancellable = true, remap = false)
    private void modifyBreakSpeed(BlockState state, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        Player player = ((Player) (Object) this);
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModTags.Items.XP_BOOSTED) || !stack.getItem().isCorrectToolForDrops(state)) return;
        float bonus = 1;
        bonus += (float) (ConfigEntries.crystallineXpBoost * player.experienceLevel / 50f);
        cir.setReturnValue(cir.getReturnValue() * bonus);
    }

    @Inject(method = "getSpeed", at = @At("HEAD"), cancellable = true)
    public void charmMovementSpeed(CallbackInfoReturnable<Float> cir) {
        if (this.hasEffect(ModEffects.CHARM.get())) cir.setReturnValue(0f);
    }

    @Override
    public Player asPlayer() {
        return (Player) (Object) this;
    }

    @Unique
    private static final String POLYPPIE_KEY = Phantasm.MOD_ID + "_CarriedPolyppie";
    @Unique
    private static final EntityDataAccessor<CompoundTag> POLYPPIE = SynchedEntityData.defineId(PlayerEntityMixin.class,
            EntityDataSerializers.COMPOUND_TAG);
    @Unique
    private PolyppieEntity polyppie = null;

    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    private void initPolyppieDataTracker(CallbackInfo ci) {
        this.getEntityData().define(POLYPPIE, new CompoundTag());
    }

    @Override
    public void setCarriedPolyppie(PolyppieEntity polyppie) {
        this.polyppie = polyppie;

        if (polyppie == null)
            this.getEntityData().set(POLYPPIE, new CompoundTag());
        else {
            this.polyppie.setYRot(180);
            this.polyppie.remove(RemovalReason.UNLOADED_WITH_PLAYER);
            this.getEntityData().set(POLYPPIE, this.polyppie.saveWithoutId(new CompoundTag()));
        }
    }

    @Override
    public PolyppieEntity getCarriedPolyppie() {
        if (!this.getEntityData().get(POLYPPIE).isEmpty() && this.level() != null && this.polyppie == null) {
            this.polyppie = ModEntities.POLYPPIE.get().create(this.level());
            this.polyppie.load(this.getEntityData().get(POLYPPIE));
            this.polyppie.tame((Player) (Object) this);
            this.polyppie.remove(RemovalReason.UNLOADED_WITH_PLAYER);
        }
        return this.polyppie;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeCustom(CompoundTag nbt, CallbackInfo ci) {
        if (this.getCarriedPolyppie() != null)
            nbt.put(POLYPPIE_KEY, this.getCarriedPolyppie().saveWithoutId(new CompoundTag()));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readCustom(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains(POLYPPIE_KEY, 10)) {
            this.getEntityData().set(POLYPPIE, nbt.getCompound(POLYPPIE_KEY));
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickMixins(CallbackInfo ci) {
        if (this.getCarriedPolyppie() != null) {
            if (this.polyppie.level() != this.level())
                this.polyppie.setLevel(this.level());

            if (this.polyppie.tickCount % 10 == 0)
                this.polyppie.setPos(this.getEyePosition().add(0, 0.5f, 0));

            this.polyppie.tick();
        }
    }

    @Inject(method = "dropEquipment", at = @At("HEAD"))
    private void dropCarriedPolyppie(CallbackInfo ci) {
        if (!this.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) && this.getCarriedPolyppie() != null)
            this.getCarriedPolyppie().setCarriedBy((Player) (Object) this, this.getEyePosition());
    }
}