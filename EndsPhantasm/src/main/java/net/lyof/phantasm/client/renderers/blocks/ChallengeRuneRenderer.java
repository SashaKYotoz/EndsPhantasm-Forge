package net.lyof.phantasm.client.renderers.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.challenge.Challenge;
import net.lyof.phantasm.block.custom.ChallengeRuneBlock;
import net.lyof.phantasm.block.entities.ChallengeRuneBlockEntity;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.entity.access.Challenger;
import net.lyof.phantasm.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ChallengeRuneRenderer implements BlockEntityRenderer<ChallengeRuneBlockEntity> {
    private final BlockRenderDispatcher blockRenderer;

    public ChallengeRuneRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public boolean shouldRenderOffScreen(ChallengeRuneBlockEntity entity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public boolean shouldRender(ChallengeRuneBlockEntity entity, Vec3 vec3d) {
        return Vec3.atCenterOf(entity.getBlockPos()).multiply(1.0, 0.0, 1.0).closerThan(vec3d.multiply(1.0, 0.0, 1.0), this.getViewDistance());
    }

    public static final ResourceLocation TOWER_BASE_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/obsidian.png");
    public static final ResourceLocation RUNE_BARRIER_TEXTURE = Phantasm.makeID("textures/particle/rune_barrier.png");

    @Override
    public void render(ChallengeRuneBlockEntity self, float tickDelta, PoseStack stack, MultiBufferSource vertexConsumers, int light, int overlay) {
        Level world = self.getLevel();
        BlockPos pos = self.getBlockPos();
        if (world == null) return;
        Player player = Minecraft.getInstance().player;

        BlockState state = ModBlocks.CHALLENGE_RUNE.get().defaultBlockState()
                .setValue(ChallengeRuneBlock.COMPLETED, self.hasCompleted(player));


        stack.pushPose();

        // Base block
        this.blockRenderer.getModelRenderer().tesselateBlock(world, this.blockRenderer.getBlockModel(state), state, pos,
                stack, vertexConsumers.getBuffer(RenderType.solid()), true, world.random,
                state.getSeed(pos), OverlayTexture.NO_OVERLAY);

        // Tower base
        if (self.renderBase) {
            light = LevelRenderer.getLightColor(world, pos.atY(-20));

            stack.pushPose();
            stack.translate(0, -pos.getY(), 0);

            RenderHelper.renderCube(stack, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    6, 7, -512, 0, -3, 4);
            RenderHelper.renderCube(stack, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    -6, -5, -512, 0, -3, 4);
            RenderHelper.renderCube(stack, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    -3, 4, -512, 0, 6, 7);
            RenderHelper.renderCube(stack, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    -3, 4, -512, 0, -6, -5);

            RenderHelper.renderCube(stack, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    5, 6, -512, 0, 4, 5);
            RenderHelper.renderCube(stack, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    4, 5, -512, 0, 5, 6);
            RenderHelper.renderCube(stack, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    5, 6, -512, 0, -4, -3);
            RenderHelper.renderCube(stack, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    4, 5, -512, 0, -5, -4);
            RenderHelper.renderCube(stack, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    -5, -4, -512, 0, 4, 5);
            RenderHelper.renderCube(stack, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    -4, -3, -512, 0, 5, 6);
            RenderHelper.renderCube(stack, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    -5, -4, -512, 0, -4, -3);
            RenderHelper.renderCube(stack, vertexConsumers, TOWER_BASE_TEXTURE, light,
                    -4, -3, -512, 0, -5, -4);

            stack.popPose();
        }

        if (self.isChallengeRunning()) {
            // Sky
            if (player instanceof Challenger challenger && challenger.isInRange(self)) {
                stack.pushPose();

                float radius = Challenge.R * Math.min(40, self.tick + tickDelta) / 40f;

                if (ConfigEntries.altChallengeBarrier) {
                    stack.mulPose(Axis.YP.rotationDegrees(self.tick + tickDelta));

                    RenderHelper.renderCube(stack, vertexConsumers.getBuffer(RenderType.eyes(RUNE_BARRIER_TEXTURE)), light,
                            -radius, radius + 1, -radius * 0.5f, radius * 1.5f + 1, -radius, radius + 1, true);
                } else {
                    RenderHelper.renderCube(stack, vertexConsumers.getBuffer(RenderType.endPortal()), light,
                            -radius, radius + 1, -radius * 0.5f, radius * 1.5f + 1, -radius, radius + 1, true);
                }

                stack.popPose();
            }
        }
        stack.popPose();
    }
}