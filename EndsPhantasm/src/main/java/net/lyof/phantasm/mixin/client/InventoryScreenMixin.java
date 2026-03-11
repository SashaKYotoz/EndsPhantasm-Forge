package net.lyof.phantasm.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.screen.DiscVisuals;
import net.lyof.phantasm.screen.access.PolyppieInventory;
import net.lyof.phantasm.screen.custom.TogglableImageButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractContainerScreen<InventoryMenu> {
    @Shadow protected abstract void init();
    @Shadow public abstract void render(GuiGraphics context, int mouseX, int mouseY, float delta);

    public InventoryScreenMixin(InventoryMenu screenHandler, Inventory playerInventory, Component text) {
        super(screenHandler, playerInventory, text);
    }

    @Unique private static final ResourceLocation INVENTORY_TEXTURE = Phantasm.makeID("textures/gui/polyppie_inventory.png");

    @Unique private Button phantasm_stop;
    @Unique private Button phantasm_play;
    @Unique private Button phantasm_hide;

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/EffectRenderingInventoryScreen;init()V"))
    private void initHeight(CallbackInfo ci) {
        if (this.menu instanceof PolyppieInventory.Handler self && self.phantasm_isVisible())
            this.imageHeight = 166 - 5 + 27;
        else this.imageHeight = 166;
    }

    @WrapOperation(method = "init", at = @At(value = "NEW", target = "(IIIIIIILnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/ImageButton;"))
    private ImageButton moveButtons(int x, int y, int width, int height, int u, int v, int hoveredVOffset,
                                    ResourceLocation texture, Button.OnPress pressAction, Operation<ImageButton> original) {
        return original.call(x, y, width, height, u, v, hoveredVOffset, texture, (Button.OnPress) button -> {
            pressAction.onPress(button);

            if (this.phantasm_hide != null) {
                this.phantasm_play.setX(this.leftPos + 145);
                this.phantasm_stop.setX(this.leftPos + 157);
                this.phantasm_hide.setX(this.leftPos + 161);
            }
        });
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (this.menu instanceof PolyppieInventory.Handler self && self.phantasm_isEnabled()
                && player instanceof PolyppieCarrier carrier) {

            this.addRenderableWidget(this.phantasm_stop = new TogglableImageButton(this.leftPos + 145, this.topPos + 180 + (self.phantasm_isVisible() ? 0 : -11), 12, 12,
                    0, 32, INVENTORY_TEXTURE, () -> carrier.getCarriedPolyppie().isPaused(),
                    button -> PolyppieInventory.Handler.onButtonClick(player, 0)));
            this.addRenderableWidget(this.phantasm_play = new ImageButton(this.leftPos + 157, this.topPos + 180 + (self.phantasm_isVisible() ? 0 : -11), 12, 12,
                    24, 32, INVENTORY_TEXTURE,
                    button -> PolyppieInventory.Handler.onButtonClick(player, 1)));

            this.addRenderableWidget(this.phantasm_hide = new TogglableImageButton(this.leftPos + 161, this.topPos + 172 + (self.phantasm_isVisible() ? 0 : -11), 8, 8,
                    0, 56, INVENTORY_TEXTURE, () -> !self.phantasm_isVisible(), button -> {
                        self.phantasm_toggleVisibility();

                        this.imageHeight = self.phantasm_isVisible() ? 166 - 5 + 27 : 166;
                        this.topPos = (this.height - this.imageHeight) / 2;
                        this.phantasm_play.active = self.phantasm_isVisible();
                        this.phantasm_stop.active = self.phantasm_isVisible();
            }));

            this.phantasm_play.active = self.phantasm_isVisible();
            this.phantasm_stop.active = self.phantasm_isVisible();
        }
    }

    @Definition(id = "imageHeight", field = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;imageHeight:I")
    @Expression("this.imageHeight")
    @ModifyExpressionValue(method = "renderBg", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int fixRenderHeight(int original) {
        if (this.menu instanceof PolyppieInventory.Handler self && self.phantasm_isVisible())
            return original + 5 - 27;
        return original;
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    private void drawPolyppieInventory(GuiGraphics context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (this.menu instanceof PolyppieInventory.Handler self && self.phantasm_isEnabled()) {
            int x = 0, y = 166 - 5;

            if (self.phantasm_isVisible() && Minecraft.getInstance().player instanceof PolyppieCarrier carrier) {
                context.blit(INVENTORY_TEXTURE, this.leftPos + x, this.topPos + y,
                        0, 0, 176, 27);

                DiscVisuals visuals = DiscVisuals.get(carrier.getCarriedPolyppie().getStack());
                x = self.phantasm_getSlotX() - 8;
                y = self.phantasm_getSlotY() - 8;

                context.drawString(this.font, carrier.getCarriedPolyppie().getName(),
                        this.leftPos + x + 32, this.topPos + y + 8, 0x373737, false);

                context.blit(visuals.notes(), this.leftPos + x, this.topPos + y,
                        0, 0, 32, 32, 32, 32);

                context.blit(visuals.progressBar(), this.leftPos + x + 32, this.topPos + y + 17, 0, 0,
                        96, 7,
                        128, 16);
                context.blit(visuals.progressBar(), this.leftPos + x + 32, this.topPos + y + 17, 0, 8,
                        (int) (carrier.getCarriedPolyppie().getSongProgress() * 96), 7,
                        128, 16);
            }
        }
    }
}
