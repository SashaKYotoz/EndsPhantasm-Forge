package net.lyof.phantasm.client;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.custom.entities.ModBlockEntities;
import net.lyof.phantasm.client.models.BehemothModel;
import net.lyof.phantasm.client.models.CrystieModel;
import net.lyof.phantasm.client.particles.ModParticles;
import net.lyof.phantasm.client.particles.custom.ZzzParticle;
import net.lyof.phantasm.client.renderers.BehemothRenderer;
import net.lyof.phantasm.client.renderers.ChoralArrowRenderer;
import net.lyof.phantasm.client.renderers.CrystieRenderer;
import net.lyof.phantasm.entities.ModEntities;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Phantasm.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.MOD_SIGN.get(), SignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MOD_HANGING_SIGN.get(), HangingSignRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CRYSTIE.get(), CrystieRenderer::new);
        event.registerEntityRenderer(ModEntities.BEHEMOTH.get(), BehemothRenderer::new);

        event.registerEntityRenderer(ModEntities.CHORAL_ARROW.get(), ChoralArrowRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CrystieModel.LAYER_LOCATION, CrystieModel::getTexturedModelData);
        event.registerLayerDefinition(BehemothModel.LAYER_LOCATION, BehemothModel::getTexturedModelData);
    }

    @SubscribeEvent
    public static void onParticleSetup(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.ZZZ.get(), ZzzParticle::provider);
    }
}
