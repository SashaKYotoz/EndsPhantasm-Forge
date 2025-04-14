package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.ITeleporter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayer.class, priority = 990)
public abstract class ServerPlayerEntityMixin extends Entity {
    @Shadow
    public abstract ServerLevel serverLevel();

    @Shadow
    private boolean seenCredits;

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

    @Inject(method = "changeDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;serverLevel()Lnet/minecraft/server/level/ServerLevel;"))
    public void cancelCreditsHead(ServerLevel destination, ITeleporter teleporter, CallbackInfoReturnable<Entity> cir) {
        Advancement advancement = this.serverLevel().getServer().getAdvancements().getAdvancement(new ResourceLocation("end/kill_dragon"));
        if (this.serverLevel().dimension() == Level.END && destination.dimension() == Level.OVERWORLD
                && advancement != null && !this.getAdvancements().getOrStartProgress(advancement).isDone()) {
            this.seenCredits = true;
        }
    }

    @Inject(method = "changeDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;sendAllPlayerInfo(Lnet/minecraft/server/level/ServerPlayer;)V"))
    public void cancelCreditsTail(ServerLevel destination, ITeleporter teleporter, CallbackInfoReturnable<Entity> cir) {
        Advancement advancement = this.serverLevel().getServer().getAdvancements().getAdvancement(new ResourceLocation("end/kill_dragon"));
        if (this.serverLevel().dimension() == Level.END && destination.dimension() == Level.OVERWORLD
                && advancement != null && !this.getAdvancements().getOrStartProgress(advancement).isDone()) {
            this.seenCredits = false;
        }
    }
}