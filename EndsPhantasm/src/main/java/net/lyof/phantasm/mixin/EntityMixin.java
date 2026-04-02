package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.mixin.access.EndGatewayBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.EndFeatures;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract Set<String> getTags();

    @ModifyReturnValue(method = "findDimensionEntryPoint", at = @At("RETURN"))
    public PortalInfo spawnInOuterEnd(PortalInfo original, ServerLevel destination) {
        if (destination.dimension() == Level.END && ConfigEntries.outerEndFirst) {
            PortalInfo result = original;
            BlockPos p = new BlockPos(1280, 60, 0);

            BlockPos pos = EndGatewayBlockEntityAccessor.getExitPos(destination, p).above(2);
            if (destination.getBlockState(pos.below(3)).isAir()) {
                destination.registryAccess().registry(Registries.CONFIGURED_FEATURE).flatMap(registry ->
                        registry.getHolder(EndFeatures.END_ISLAND)).ifPresent(reference ->
                        reference.get().place(destination, destination.getChunkSource().getGenerator(),
                                RandomSource.create(pos.asLong()), pos.below(2)));
            }

            original = new PortalInfo(new Vec3(pos.getX(), pos.getY(), pos.getZ()), result.speed, result.yRot, result.xRot);
        }
        return original;
    }

    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    public void charmCursor(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if (((Entity) (Object) this) instanceof LivingEntity living && living.hasEffect(ModEffects.CHARM.get()))
            ci.cancel();
    }
    @ModifyReturnValue(method = "getTypeName", at = @At("RETURN"))
    public Component setChallengeName(Component original) {
        if (this.getTags().contains(Phantasm.MOD_ID + ".challenge"))
            return Component.translatable("entity.phantasm.challenge", original);
        return original;
    }
}