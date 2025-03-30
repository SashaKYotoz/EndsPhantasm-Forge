package net.lyof.phantasm.mixin;

import net.lyof.phantasm.Phantasm;
import net.minecraft.client.renderer.entity.VexRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Vex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VexRenderer.class)
public class VexRenderMixin {
    @Unique
    private static final ResourceLocation TEXTURE = Phantasm.makeID("textures/entity/ender_vex.png");
    @Unique
    private static final ResourceLocation CHARGING_TEXTURE = Phantasm.makeID("textures/entity/ender_vex_charging.png");

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/monster/Vex;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    public void enderTowerTexture(Vex vex, CallbackInfoReturnable<ResourceLocation> cir) {
        if (vex.level().dimension().location().toString().equals("minecraft:the_end"))
            cir.setReturnValue(vex.isCharging() ? CHARGING_TEXTURE : TEXTURE);
    }
}