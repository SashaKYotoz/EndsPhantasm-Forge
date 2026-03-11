package net.lyof.phantasm.mixin.client;

import net.lyof.phantasm.mixin.access.AbstractContainerScreenAccessor;
import net.lyof.phantasm.screen.access.PolyppieInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractWidget.class)
public class AbstractWidgetMixin {
    @Shadow
    private int y;
    @Shadow private int x;

    /**
     * @author Lyof (End's Phantasm)
     * @reason No idea why but regular mixins do not work
     */
    @Overwrite
    public int getY() {
        if (
                Minecraft.getInstance().screen instanceof AbstractContainerScreen<?> handled
                        && handled instanceof AbstractContainerScreenAccessor accessor
                        && accessor.getMenu() instanceof PolyppieInventory.Handler handler
                        && handler.phantasm_isVisible()
                        && accessor.getX() <= this.x && accessor.getX() + accessor.getImageWidth() >= this.x
                        && accessor.getY() <= this.y && accessor.getY() + accessor.getImageHeight() >= this.y
        )
            return this.y - 11;
        return this.y;
    }
}