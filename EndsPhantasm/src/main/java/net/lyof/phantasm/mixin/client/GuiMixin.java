package net.lyof.phantasm.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.entity.access.Corrosive;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Shadow
    protected abstract Player getCameraPlayer();

    @Unique
    private static final ResourceLocation VANILLA_ICONS = ResourceLocation.withDefaultNamespace("textures/gui/icons.png");
    @Unique private static final ResourceLocation VANILLA_WIDGETS = ResourceLocation.withDefaultNamespace("textures/gui/widgets.png");

    @Unique private static final ResourceLocation CORROSION_ARMOR = Phantasm.makeID("textures/gui/corrosion_armor.png");
    @Unique private static final ResourceLocation CORROSION_ATTACK = Phantasm.makeID("textures/gui/corrosion_attack_indicator.png");
    @Unique private static final ResourceLocation CORROSION_HOTBAR = Phantasm.makeID("textures/gui/corrosion_hotbar.png");

    @WrapOperation(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"))
    public void renderCorrodedArmor(GuiGraphics instance, ResourceLocation texture, int x, int y, int u, int v, int width, int height,
                                    Operation<Void> original) {

        if (v == 9 && (u == 16 || u == 25 || u == 34) && texture.equals(VANILLA_ICONS)
                && this.getCameraPlayer().hasEffect(ModEffects.CORROSION.get())) {

            int offset = (int) Math.round(Math.pow(Math.sin((x + this.getCameraPlayer().level().getDayTime()) / 2f), 2));
            instance.blit(CORROSION_ARMOR, x, y + offset, 0, 0, 9, 9, 9, 9);
        }
        else
            original.call(instance, texture, x, y, u, v, width, height);
    }

    @WrapOperation(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"))
    public void renderCorrosiveCrosshair(GuiGraphics instance, ResourceLocation texture, int x, int y, int u, int v, int width, int height,
                                         Operation<Void> original) {

        if (v == 94 && (u == 52 || u == 68) && texture.equals(VANILLA_ICONS)
                && this.getCameraPlayer() instanceof Corrosive corrosive && corrosive.isCorrosive()) {

            instance.blit(CORROSION_ATTACK, x, y, u - 52, 18, width, 16, 32, 32);
        }
        else
            original.call(instance, texture, x, y, u, v, width, height);
    }

    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"))
    public void renderCorrosiveHotbar(GuiGraphics instance, ResourceLocation texture, int x, int y, int u, int v, int width, int height,
                                      Operation<Void> original, @Local(ordinal = 2) int p) {

        if (this.getCameraPlayer() instanceof Corrosive corrosive && corrosive.isCorrosive()) {
            if (texture.equals(VANILLA_WIDGETS) && u == 0 && v == 22)
                instance.blit(CORROSION_HOTBAR, x - 4, y - 4, 0, 0, 32, 32, 32, 32);
            else if (texture.equals(VANILLA_ICONS) && u == 18 && v == 112 - p)
                instance.blit(CORROSION_ATTACK, x, y, 0, v - 94, width, height, 32, 32);

            else original.call(instance, texture, x, y, u, v, width, height);
        }
        else original.call(instance, texture, x, y, u, v, width, height);
    }
}