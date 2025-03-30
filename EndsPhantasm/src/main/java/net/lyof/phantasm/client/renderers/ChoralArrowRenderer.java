package net.lyof.phantasm.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entities.custom.ChoralArrowEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ChoralArrowRenderer extends ArrowRenderer<ChoralArrowEntity> {
    private static final ResourceLocation TEXTURE = Phantasm.makeID("textures/entity/choral_arrow.png");

    public ChoralArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ChoralArrowEntity entity, float f, float g, PoseStack stack, MultiBufferSource source, int i) {
        if (!entity.shotByCrossbow)
            super.render(entity, f, g, stack, source, i);
    }

    @Override
    public ResourceLocation getTextureLocation(ChoralArrowEntity entity) {
        return TEXTURE;
    }
}
