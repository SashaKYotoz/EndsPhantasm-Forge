package net.lyof.phantasm.client;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.entities.ModBlockEntities;
import net.lyof.phantasm.client.models.BehemothModel;
import net.lyof.phantasm.client.models.CrystieModel;
import net.lyof.phantasm.client.models.PolyppieModel;
import net.lyof.phantasm.client.particles.ModParticles;
import net.lyof.phantasm.client.particles.custom.ZzzParticle;
import net.lyof.phantasm.client.renderers.*;
import net.lyof.phantasm.client.renderers.blocks.ChallengeRuneRenderer;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.setup.ReloadListener;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Phantasm.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.MOD_SIGN.get(), SignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MOD_HANGING_SIGN.get(), HangingSignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CHALLENGE_RUNE.get(), ChallengeRuneRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CRYSTIE.get(), CrystieRenderer::new);
        event.registerEntityRenderer(ModEntities.BEHEMOTH.get(), BehemothRenderer::new);
        event.registerEntityRenderer(ModEntities.POLYPPIE.get(), PolyppieRenderer::new);
        event.registerEntityRenderer(ModEntities.SOUR_SLUDGE.get(), SourSludgeRenderer::new);

        event.registerEntityRenderer(ModEntities.CHORAL_ARROW.get(), ChoralArrowRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CrystieModel.LAYER_LOCATION, CrystieModel::getTexturedModelData);
        event.registerLayerDefinition(BehemothModel.LAYER_LOCATION, BehemothModel::getTexturedModelData);
        event.registerLayerDefinition(PolyppieModel.LAYER_LOCATION, PolyppieModel::getTexturedModelData);
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.AddLayers event) {
        event.getSkins().forEach((s) -> {
            LivingEntityRenderer<? extends Player, ? extends EntityModel<? extends Player>> livingEntityRenderer = event.getSkin(s);
            if (livingEntityRenderer instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new PolyppieRenderer.CarriedRenderer(playerRenderer));
            }
        });
    }

    @SubscribeEvent
    public static void onParticleSetup(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.ZZZ.get(), ZzzParticle::provider);
    }
    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new ReloadListener.Client());
    }
}
