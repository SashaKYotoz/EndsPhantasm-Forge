package net.lyof.phantasm.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entities.custom.CrystieEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class CrystieModel<T extends CrystieEntity> extends HierarchicalModel<CrystieEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Phantasm.MOD_ID, "crystie_model"), "main");
    private final ModelPart main;
    private final ModelPart wingsleft;
    private final ModelPart wingsright;
    private final ModelPart root;

    public CrystieModel(ModelPart root) {
        this.main = root.getChild("main");
        this.wingsleft = main.getChild("wings").getChild("left");
        this.wingsright = main.getChild("wings").getChild("right");
        this.root = root;
    }
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition main = modelPartData.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        //ModelPartData spike = main.addChild("spike", ModelPartBuilder.create().uv(12, 12).cuboid(-3.0F, -6.0F, 0.0F, 6.0F, 7.0F, 0.0F, new Dilation(0.0F))
        //		.uv(0, 6).cuboid(0.0F, -6.0F, -3.0F, 0.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -7.0F, 0.0F));

        PartDefinition wings = main.addOrReplaceChild("wings", CubeListBuilder.create(), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition left = wings.addOrReplaceChild("left", CubeListBuilder.create().texOffs(0, 19).mirror().addBox(2.0F, -1.0F, 2.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(12, 19).mirror().addBox(2.0F, 4.0F, 2.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right = wings.addOrReplaceChild("right", CubeListBuilder.create().texOffs(0, 19).addBox(-8.0F, -1.0F, 2.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(12, 19).addBox(-5.0F, 4.0F, 2.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(modelData, 32, 32);
    }
    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer consumer, int light, int overlay, int color) {
        main.render(stack,consumer,light,overlay,color);
    }

    @Override
    public void setupAnim(CrystieEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.wingsleft.yRot = (float) Math.cos(0.01 * ageInTicks * 180 / Math.PI) / 3;
        this.wingsright.yRot = (float) -Math.cos(0.01 * ageInTicks * 180 / Math.PI) / 3;
    }
}
