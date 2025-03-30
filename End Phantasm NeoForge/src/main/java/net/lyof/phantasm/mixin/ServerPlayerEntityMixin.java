package net.lyof.phantasm.mixin;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayer.class, priority = 900)
public abstract class ServerPlayerEntityMixin extends Entity {
    @Shadow
    public abstract ServerLevel serverLevel();

    @Shadow
    public boolean seenCredits;

    @Shadow
    public abstract PlayerAdvancements getAdvancements();

    public ServerPlayerEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "changeDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;serverLevel()Lnet/minecraft/server/level/ServerLevel;"))
    public void cancelCreditsHead(DimensionTransition transition, CallbackInfoReturnable<Entity> cir) {
        AdvancementHolder advancement = this.serverLevel().getServer().getAdvancements().get(ResourceLocation.withDefaultNamespace("end/kill_dragon"));
        if (this.serverLevel().dimension() == Level.END && transition.newLevel().dimension() == Level.OVERWORLD
                && advancement != null && !this.getAdvancements().getOrStartProgress(advancement).isDone()) {
            this.seenCredits = true;
        }
    }

    @Inject(method = "changeDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;sendAllPlayerInfo(Lnet/minecraft/server/level/ServerPlayer;)V"))
    public void cancelCreditsTail(DimensionTransition transition, CallbackInfoReturnable<Entity> cir) {
        AdvancementHolder advancement = this.serverLevel().getServer().getAdvancements().get(ResourceLocation.withDefaultNamespace("end/kill_dragon"));
        if (this.serverLevel().dimension() == Level.END && transition.newLevel().dimension() == Level.OVERWORLD
                && advancement != null && !this.getAdvancements().getOrStartProgress(advancement).isDone()) {
            this.seenCredits = false;
        }
    }
}