package net.lyof.phantasm.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class PolyppieModel<T extends PolyppieEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Phantasm.MOD_ID, "polyppie_model"), "main");
	private final ModelPart main;
	private final ModelPart head;
	private final ModelPart legs;
	private final ModelPart legs_primary;
	private final ModelPart legs_secondary;

	public PolyppieModel(ModelPart root) {
		this.main = root.getChild("main");
		this.head = this.main.getChild("head");
		this.legs = this.main.getChild("legs");
		this.legs_primary = this.legs.getChild("legs_primary");
		this.legs_secondary = this.legs.getChild("legs_secondary");
	}

	public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition main = modelPartData.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -8.0F, -3.5F, 12.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(31, 0).addBox(-6.0F, -11.0F, 0.0F, 12.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition legs = main.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition legs_primary = legs.addOrReplaceChild("legs_primary", CubeListBuilder.create().texOffs(0, 14).addBox(-5.0F, -1.0F, -2.0F, 10.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 15).addBox(-5.0F, -1.0F, 0.0F, 10.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 16).addBox(-5.0F, -1.0F, 2.0F, 10.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition legs_secondary = legs.addOrReplaceChild("legs_secondary", CubeListBuilder.create().texOffs(0, 17).addBox(-5.0F, -1.0F, -2.0F, 10.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 18).addBox(-5.0F, -1.0F, 0.0F, 10.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 19).addBox(-5.0F, -1.0F, 2.0F, 10.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(modelData, 64, 64);
	}

	@Override
	public void setupAnim(PolyppieEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.root().render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.main;
	}
}