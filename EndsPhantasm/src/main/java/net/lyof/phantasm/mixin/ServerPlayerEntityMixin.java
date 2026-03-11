package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.setup.ModPackets;
import net.lyof.phantasm.setup.packets.BeginCutsceneStartsPacket;
import net.lyof.phantasm.util.MixinAccess;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.ITeleporter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayer.class, priority = 990)
public abstract class ServerPlayerEntityMixin extends Entity implements MixinAccess<Boolean> {
    @Shadow
    public abstract ServerLevel serverLevel();

    @Shadow
    private boolean seenCredits;
    @Shadow
    private boolean isChangingDimension;
    @Shadow
    public boolean wonGame;

    @Unique
    private static final ResourceLocation CREDITS_ADVANCEMENT = ResourceLocation.fromNamespaceAndPath("minecraft", "end/kill_dragon");

    @Unique
    private boolean seenBeginning = false;

    @Shadow
    public abstract PlayerAdvancements getAdvancements();

    public ServerPlayerEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @WrapOperation(method = "lambda$changeDimension$8", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;createEndPlatform(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V"))
    public void noPlatform(ServerPlayer instance, ServerLevel serverlevel, BlockPos pos, Operation<Void> original) {
        BlockPos.MutableBlockPos mutable = ServerLevel.END_SPAWN_POINT.mutable();
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                for (int k = -1; k < 3; ++k) {
                    BlockState blockState = k == -1 ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.AIR.defaultBlockState();
                    serverlevel.setBlockAndUpdate(mutable.set(ServerLevel.END_SPAWN_POINT).move(Direction.DOWN, 2).move(j, k, i), blockState);
                }
            }
        }
    }

    @WrapMethod(method = "Lnet/minecraft/server/level/ServerPlayer;changeDimension(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraftforge/common/util/ITeleporter;)Lnet/minecraft/world/entity/Entity;", remap = false)
    public Entity cancelCredits(ServerLevel destination, ITeleporter iTeleporter, Operation<Entity> original) {
        ServerPlayer self = (ServerPlayer) (Object) this;
        this.wonGame = false;

        if (destination.dimension() == Level.END) {
            if (!this.seenBeginning && ConfigEntries.beginCutscene) {
                this.isChangingDimension = true;
                self.unRide();
                this.serverLevel().removePlayerImmediately(self, RemovalReason.CHANGED_DIMENSION);

                if (!this.wonGame) {
                    this.wonGame = true;
                    ModPackets.sendToPlayer(new BeginCutsceneStartsPacket(), self);
                }

                return this;
            }
        }

        Advancement advancement = this.serverLevel().getServer().getAdvancements().getAdvancement(CREDITS_ADVANCEMENT);
        if (this.serverLevel().dimension() == Level.END && destination.dimension() == Level.OVERWORLD
                && advancement != null && !this.getAdvancements().getOrStartProgress(advancement).isDone()) {

            this.seenCredits = true;
            Entity result = original.call(destination, iTeleporter);
            this.seenCredits = false;
            return result;
        }

        return original.call(destination, iTeleporter);
    }

    @Unique
    private static final String SEEN_BEGINNING_KEY = Phantasm.MOD_ID + "_seenBeginning";

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void readData(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains(SEEN_BEGINNING_KEY)) this.seenBeginning = nbt.getBoolean(SEEN_BEGINNING_KEY);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void writeData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putBoolean(SEEN_BEGINNING_KEY, this.seenBeginning);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "restoreFrom", at = @At("HEAD"))
    private void copySeenBeginning(ServerPlayer old, boolean alive, CallbackInfo ci) {
        this.seenBeginning = ((MixinAccess<Boolean>) old).getMixinValue();

        if ((alive || this.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY))
                && old instanceof PolyppieCarrier oldCarrier
                && this instanceof PolyppieCarrier carrier && oldCarrier.getCarriedPolyppie() != null) {
            carrier.setCarriedPolyppie(oldCarrier.getCarriedPolyppie());
        }
    }

    @Override
    public void setMixinValue(Boolean value) {
        this.seenBeginning = value;
    }

    @Override
    public Boolean getMixinValue() {
        return this.seenBeginning;
    }
}