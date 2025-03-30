package net.lyof.phantasm.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entities.animations.BehemothAnimation;
import net.lyof.phantasm.entities.custom.BehemothEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BehemothModel<T extends BehemothEntity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Phantasm.MOD_ID, "behemoth_model"), "main");
	public static final float rad = (float) Math.PI / 180f;

	private final ModelPart body;
	private final ModelPart left_horn;
	private final ModelPart right_horn;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart zzz;

	public BehemothModel(ModelPart root) {
		this.body = root.getChild("body");
		this.left_horn = this.body.getChild("left_horn");
		this.right_horn = this.body.getChild("right_horn");
		this.left_leg = this.body.getChild("left_leg");
		this.right_leg = this.body.getChild("right_leg");
		this.zzz = root.getChild("zzz");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -28.0F, -9.0F, 18.0F, 28.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(0, 87).addBox(-8.0F, -27.0F, -7.0F, 16.0F, 27.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 47).addBox(-9.0F, 0.0F, -9.0F, 18.0F, 2.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition left_horn = body.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(65, 39).addBox(0.0F, -6.0F, -9.0F, 6.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, -26.0F, 5.0F));
		PartDefinition right_horn = body.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(65, 39).mirror().addBox(-6.0F, -6.0F, -9.0F, 6.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-9.0F, -26.0F, 5.0F));

		PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(55, 0).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 0.0F));
		PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(55, 0).mirror().addBox(-3.0F, -2.0F, -3.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 0.0F, 0.0F));

		PartDefinition zzz = modelPartData.addOrReplaceChild("zzz", CubeListBuilder.create().texOffs(60, 101).addBox(0, 0, 0, 8, 8, 0, new CubeDeformation(0)), PartPose.offset(8, 0, 8));

		return LayerDefinition.create(modelData, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		switch (entity.animation) {
			case WALKING -> {
				this.body.xRot = Mth.sin(entity.animTicks * 3 * rad) * 0.1f;
				this.left_leg.xRot = Mth.cos(limbSwing * 0.6662f + (float) Math.PI) * 1.4f * limbSwingAmount;
				this.right_leg.xRot = Mth.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
			}
			case SLEEPING -> {
				this.body.xRot = rad * -90;
				this.body.zScale = (float) (1 + Math.sin(entity.animTicks * rad) * 0.1);
				this.left_horn.zScale = 1;
				this.right_horn.zScale = 1;
			}
			case WAKING_UP -> {
				this.root().getAllParts().forEach(ModelPart::resetPose);
				this.body.xRot = Mth.cos(rad * entity.animTicks * 90f / BehemothAnimation.WAKING_UP.maxTime) * rad * -90;
			}
			case WAKING_DOWN -> {
				this.root().getAllParts().forEach(ModelPart::resetPose);
				this.body.xRot = Mth.sin(rad * entity.animTicks * 90f / BehemothAnimation.WAKING_DOWN.maxTime) * rad * -90;
			}
		}
		//this.getPart().traverse().forEach(ModelPart::resetTransform);

		//this.updateAnimation(entity.sleepingAnimationState, ModAnimations.Behemoth.SLEEPING, ageInTicks);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer consumer, int light, int overlay,int color) {
		this.root().render(stack, consumer, light, overlay,color);
	}

	@Override
	public ModelPart root() {
		return this.body;
	}
}