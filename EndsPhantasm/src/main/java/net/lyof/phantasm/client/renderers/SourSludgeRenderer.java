package net.lyof.phantasm.client.renderers;

import net.lyof.phantasm.Phantasm;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Slime;

public class SourSludgeRenderer extends SlimeRenderer {
    private static final ResourceLocation TEXTURE = Phantasm.makeID("textures/entity/sour_sludge/sour_sludge.png");
    public SourSludgeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Slime slime) {
        return TEXTURE;
    }
}
