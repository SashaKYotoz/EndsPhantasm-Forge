package net.lyof.phantasm.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lyof.phantasm.client.models.PolyppieModel;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;

public class PolyppieRenderer extends MobRenderer<PolyppieEntity, PolyppieModel<PolyppieEntity>> {
    private static final float pi = (float) Math.PI;
    private static final Map<String, Quaternionf> rotationCache = new HashMap<>();

    private static PolyppieRenderer instance = null;

    private static Quaternionf getRotation(float x, float y, float z) {
        String key = x + " " + y + " " + z;
        rotationCache.putIfAbsent(key, new Quaternionf().rotateXYZ(x, y, z));
        return rotationCache.get(key);
    }

    public PolyppieRenderer(EntityRendererProvider.Context context) {
        super(context, new PolyppieModel<>(context.bakeLayer(PolyppieModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new DiscRenderer(this, context.getItemRenderer()));
        instance = this;
    }

    @Override
    public ResourceLocation getTextureLocation(PolyppieEntity entity) {
        return entity.getVariant().texture;
    }

    public static class DiscRenderer extends RenderLayer<PolyppieEntity, PolyppieModel<PolyppieEntity>> {
        private final ItemRenderer itemRenderer;

        public DiscRenderer(RenderLayerParent<PolyppieEntity, PolyppieModel<PolyppieEntity>> context, ItemRenderer itemRenderer) {
            super(context);
            this.itemRenderer = itemRenderer;
        }

        @Override
        public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, PolyppieEntity entity,
                           float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

            matrices.pushPose();
            matrices.translate(0.015, 1.15, entity.isPlayingRecord() ?
                    -0.05 - 0.1 * Mth.cos((entity.tickCount - entity.recordStartTick + tickDelta) * 0.08f) : -0.15);
            matrices.mulPose(getRotation(-pi / 2, 0, 0));
            this.itemRenderer.renderStatic(entity.getStack(), ItemDisplayContext.GROUND, light, 0, matrices, vertexConsumers, entity.level(), 0);
            matrices.popPose();
        }
    }


    public static class CarriedRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
        public CarriedRenderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> context) {
            super(context);
        }

        @Override
        public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, AbstractClientPlayer self,
                           float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

            if (self instanceof PolyppieCarrier carrier && carrier.getCarriedPolyppie() != null) {
                matrices.pushPose();

                ModelPart torso = this.getParentModel().body;
                matrices.scale(torso.xScale, torso.yScale, torso.zScale);
                matrices.translate(0, torso.yScale * 0.6, torso.zScale * (self.isCrouching() ? 0.6 : 0.35));
                matrices.mulPose(getRotation(torso.xRot + pi, 0, torso.zRot));

                instance.render(carrier.getCarriedPolyppie(), tickDelta, animationProgress, matrices, vertexConsumers, light);
                matrices.popPose();
            }
        }
    }
}
