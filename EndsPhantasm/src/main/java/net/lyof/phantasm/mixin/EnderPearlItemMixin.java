package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.EnderpearlItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnderpearlItem.class)
public class EnderPearlItemMixin {
    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ThrownEnderpearl;shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"))
    private void removeRandom(ThrownEnderpearl instance, Entity entity, float pitch, float yaw, float roll, float speed, float spread, Operation<Void> original) {
        original.call(instance, entity, pitch, yaw, roll, speed, 0f);
    }
}
