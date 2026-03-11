package net.lyof.phantasm.screen.custom;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class TogglableImageButton extends ImageButton {
    public TogglableImageButton(int x, int y, int width, int height, int u, int v, ResourceLocation texture, Supplier<Boolean> toggle, Button.OnPress pressAction) {
        super(x, y, width, height, u, v, texture, pressAction);
        this.toggled = toggle;
    }

    protected Supplier<Boolean> toggled;

    @Override
    public void renderTexture(GuiGraphics context, ResourceLocation texture, int x, int y, int u, int v, int hoveredVOffset, int width, int height, int textureWidth, int textureHeight) {
        if (!this.active) return;

        this.setFocused(false);
        super.renderTexture(context, texture, x, y, this.toggled.get() ? u + this.width : u, v, hoveredVOffset, width, height,
                textureWidth, textureHeight);
    }
}