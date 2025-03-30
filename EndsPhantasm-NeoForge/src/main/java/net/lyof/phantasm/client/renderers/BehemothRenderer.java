package net.lyof.phantasm.client.renderers;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.client.models.BehemothModel;
import net.lyof.phantasm.entities.custom.BehemothEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BehemothRenderer extends MobRenderer<BehemothEntity, BehemothModel<BehemothEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Phantasm.MOD_ID, "textures/entity/behemoth.png");
    private static final ResourceLocation TEXTURE_ANGRY = ResourceLocation.fromNamespaceAndPath(Phantasm.MOD_ID, "textures/entity/behemoth_angry.png");

    public BehemothRenderer(EntityRendererProvider.Context context) {
        super(context, new BehemothModel<>(context.bakeLayer(BehemothModel.LAYER_LOCATION)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(BehemothEntity entity) {
        return entity.isAngry() ? TEXTURE_ANGRY : TEXTURE;
    }
}