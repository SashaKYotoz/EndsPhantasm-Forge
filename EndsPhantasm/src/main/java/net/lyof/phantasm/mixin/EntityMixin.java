package net.lyof.phantasm.mixin;

import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.mixin.access.EndGatewayBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.EndFeatures;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "findDimensionEntryPoint", at = @At("RETURN"), cancellable = true)
    public void spawnInOuterEnd(ServerLevel destination, CallbackInfoReturnable<PortalInfo> cir) {
        if (destination.dimension() == Level.END && ConfigEntries.outerEndIntegration) {
            PortalInfo result = cir.getReturnValue();
            BlockPos p = new BlockPos(1280, 60, 0);

            BlockPos pos = EndGatewayBlockEntityAccessor.getExitPos(destination, p);
            if (destination.getBlockState(pos.below()).isAir()) {
                destination.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE)
                        .getOptional(EndFeatures.END_ISLAND).ifPresent(reference ->
                                reference.place(destination, destination.getChunkSource().getGenerator(),
                                        RandomSource.create(pos.asLong()), pos.below(2)));
            }
            result = new PortalInfo(new Vec3(pos.getX(), pos.getY() + 2, pos.getZ()), result.speed, result.yRot, result.xRot);

            cir.setReturnValue(result);
        }
    }

    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    public void charmCursor(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if (((Entity) (Object) this) instanceof LivingEntity living && living.hasEffect(ModEffects.CHARM.get()))
            ci.cancel();
    }
}