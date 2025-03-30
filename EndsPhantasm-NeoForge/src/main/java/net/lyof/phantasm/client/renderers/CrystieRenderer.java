package net.lyof.phantasm.client.renderers;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.client.models.CrystieModel;
import net.lyof.phantasm.entities.custom.CrystieEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class CrystieRenderer extends MobRenderer<CrystieEntity, CrystieModel<CrystieEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Phantasm.MOD_ID,"textures/entity/crystie.png");
    public CrystieRenderer(EntityRendererProvider.Context context) {
        super(context, new CrystieModel<>(context.bakeLayer(CrystieModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new EyesLayer<>(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(TEXTURE);
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(CrystieEntity crystie) {
        return TEXTURE;
    }
}
