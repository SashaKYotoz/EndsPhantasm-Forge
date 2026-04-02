package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.lyof.phantasm.screen.access.PolyppieInventory;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {
    @WrapWithCondition(method = "selectTab", at = @At(value = "INVOKE",ordinal = 2, target = "Lnet/minecraft/core/NonNullList;add(Ljava/lang/Object;)Z"))
    private <E> boolean hidePolyppieSlot(NonNullList instance, Object o) {
        return o instanceof Slot slot && !(slot.container instanceof PolyppieInventory);
    }
}