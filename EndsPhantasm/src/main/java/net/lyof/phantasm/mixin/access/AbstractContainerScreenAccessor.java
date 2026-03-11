package net.lyof.phantasm.mixin.access;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor {
    @Accessor
    <T extends AbstractContainerMenu> T getMenu();

    @Accessor("leftPos")
    int getX();

    @Accessor("topPos")
    int getY();

    @Accessor
    int getImageWidth();

    @Accessor
    int getImageHeight();
}