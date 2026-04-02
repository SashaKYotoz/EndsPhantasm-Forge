package net.lyof.phantasm.mixin.client;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.util.MixinAccess;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Mixin(WinScreen.class)
public abstract class WinScreenMixin implements MixinAccess<Boolean> {
    @Shadow
    private List<FormattedCharSequence> lines;
    @Shadow private IntSet centeredLines;
    @Shadow private int totalScrollLength;

    @Shadow protected abstract void wrapCreditsIO(String id, WinScreen.CreditsReader reader);
    @Shadow protected abstract void addPoemFile(Reader reader) throws IOException;

    @Unique
    protected final ResourceLocation BACKGROUND_TEXTURE = Phantasm.makeID("textures/gui/credits_background.png");

    @Unique private boolean beginningCredits = false;

    @WrapMethod(method = "init")
    private void initBeginning(Operation<Void> original) {
        original.call();
        if (this.beginningCredits && this.lines == null) {
            this.lines = Lists.newArrayList();
            this.centeredLines = new IntOpenHashSet();
            this.wrapCreditsIO("phantasm:texts/begin.txt", this::addPoemFile);

            this.totalScrollLength = this.lines.size() * 12 - 200;
        }
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/LogoRenderer;renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V"))
    private boolean cancelLogoDraw(LogoRenderer instance, GuiGraphics context, int screenWidth, float alpha, int y) {
        return !this.beginningCredits;
    }

    @WrapOperation(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIFFIIII)V"))
    private void moveBackground(GuiGraphics instance, ResourceLocation texture, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight, Operation<Void> original) {
        if (this.beginningCredits) original.call(instance, BACKGROUND_TEXTURE, x, y, z, u, v, width, height, textureWidth, textureHeight);
        original.call(instance, texture, x, y, z, u, v, width, height, textureWidth, textureHeight);
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "CONSTANT", args = "intValue=100"))
    private int removeLogoSpace(int constant) {
        if (this.beginningCredits) return 0;
        return constant;
    }

    @Override
    public void setMixinValue(Boolean value) {
        this.beginningCredits = value;
    }

    @Override
    public Boolean getMixinValue() {
        return this.beginningCredits;
    }
}