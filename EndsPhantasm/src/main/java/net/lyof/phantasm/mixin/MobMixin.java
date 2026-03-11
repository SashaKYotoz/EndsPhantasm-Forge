package net.lyof.phantasm.mixin;

import net.lyof.phantasm.effect.ModEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "serverAiStep", at = @At("HEAD"), cancellable = true)
    public void charmAi(CallbackInfo ci) {
        if (this.hasEffect(ModEffects.CHARM.get())) ci.cancel();
    }

    @Inject(method = "finalizeSpawn", at = @At("HEAD"))
    private void initializeBounds(ServerLevelAccessor accessor, DifficultyInstance instance, MobSpawnType type, SpawnGroupData data, CompoundTag tag, CallbackInfoReturnable<SpawnGroupData> cir) {

        if ((Mob) (Object) this instanceof Vex vex && type == MobSpawnType.SPAWNER) {
            vex.setBoundOrigin(vex.getOnPos());
        }
    }
}