package net.lyof.phantasm.mixin;

import net.lyof.phantasm.Phantasm;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NearestAttackableTargetGoalMixin extends TargetGoal {
    @Shadow
    @Nullable
    protected LivingEntity target;

    public NearestAttackableTargetGoalMixin(Mob mob, boolean checkVisibility) {
        super(mob, checkVisibility);
    }

    @Inject(method = "findTarget", at = @At("TAIL"))
    public void preventChallengeTargeting(CallbackInfo ci) {
        if (this.mob.getType() == EntityType.ENDERMAN && this.target != null &&
                this.target.getType() == EntityType.ENDERMITE && this.target.getTags().contains(Phantasm.MOD_ID + ".challenge"))
            this.target = null;
    }
}