package net.lyof.phantasm;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.custom.entities.ModBlockEntities;
import net.lyof.phantasm.client.particles.ModParticles;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.config.ModConfig;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.entities.ModEntities;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.item.ModTabs;
import net.lyof.phantasm.setup.ModSounds;
import net.lyof.phantasm.world.ModFeatures;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.lyof.phantasm.world.biome.ModBiomes;
import net.lyof.phantasm.world.feature.tree.ModFoliageTypes;
import net.lyof.phantasm.world.feature.tree.ModTrunkTypes;
import net.lyof.phantasm.world.gen.TheEndBiomes;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Phantasm.MOD_ID)
public class Phantasm {
    public static final String MOD_ID = "phantasm";
    public static final Logger LOGGER = LoggerFactory.getLogger("End's Phantasm");

    public Phantasm(IEventBus modEventBus) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModTrunkTypes.TRUNK_TYPE_REGISTRY.register(modEventBus);
        ModFoliageTypes.FOLIAGE_PLACER_REGISTRY.register(modEventBus);
        ModTabs.CREATIVE_TAB.register(modEventBus);
        ModFeatures.REGISTRY.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModParticles.PARTICLE_TYPES.register(modEventBus);
        EndDataCompat.register();
        ModEffects.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModConfig.register();
        modEventBus.addListener(this::commonSetup);
        if (FMLEnvironment.dist.isClient())
            modEventBus.addListener(this::clientSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        if (ConfigEntries.doDreamingDenBiome)
            event.enqueueWork(() -> TheEndBiomes.addHighlandsBiome(ModBiomes.DREAMING_DEN, ConfigEntries.dreamingDenWeight));
        if (ConfigEntries.doAcidburntAbyssesBiome)
            event.enqueueWork(() -> TheEndBiomes.addHighlandsBiome(ModBiomes.ACIDBURNT_ABYSSES, ConfigEntries.acidburntAbyssesWeight));
    }

    private void clientSetup(FMLClientSetupEvent event) {
        for (DeferredBlock block : ModBlocks.BLOCK_CUTOUT)
            ItemBlockRenderTypes.setRenderLayer((Block) block.get(), RenderType.cutout());
    }

    public static <T> T log(T message) {
        LOGGER.info(String.valueOf(message));
        return message;
    }
    public static ResourceLocation makeID(String id) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, id);
    }
}