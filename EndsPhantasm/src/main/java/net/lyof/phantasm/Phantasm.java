package net.lyof.phantasm;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.custom.entities.ModBlockEntities;
import net.lyof.phantasm.client.particles.ModParticles;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.config.ModConfig;
import net.lyof.phantasm.effect.BetterBrewingRecipe;
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
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Phantasm.MOD_ID)
public class Phantasm {
    public static final String MOD_ID = "phantasm";
    public static final Logger LOGGER = LoggerFactory.getLogger("End's Phantasm");

    public Phantasm() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModTrunkTypes.TRUNK_TYPE_REGISTRY.register(modEventBus);
        ModFoliageTypes.FOLIAGE_PLACER_REGISTRY.register(modEventBus);
        ModTabs.CREATIVE_TAB.register(modEventBus);
        ModFeatures.REGISTRY.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModParticles.PARTICLE_TYPES.register(modEventBus);
        EndDataCompat.register();
        ModEffects.register(modEventBus);
        ModSounds.register();
        ModConfig.register();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
    }

    public static ResourceLocation makeID(String id) {
        return new ResourceLocation(MOD_ID, id);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        if (ConfigEntries.doDreamingDenBiome)
            event.enqueueWork(() -> TheEndBiomes.addHighlandsBiome(ModBiomes.DREAMING_DEN, ConfigEntries.dreamingDenWeight));
        if (ConfigEntries.doAcidburntAbyssesBiome)
            event.enqueueWork(() -> TheEndBiomes.addHighlandsBiome(ModBiomes.ACIDBURNT_ABYSSES, ConfigEntries.acidburntAbyssesWeight));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, ModItems.POME_SLICE.get(), ModEffects.CORROSION_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(ModEffects.CORROSION_POTION.get(), Items.REDSTONE, ModEffects.LONG_CORROSION_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(ModEffects.CORROSION_POTION.get(), Items.GLOWSTONE_DUST, ModEffects.STRONG_CORROSION_POTION.get()));
    }

    private void clientSetup(FMLClientSetupEvent event) {
        for (RegistryObject<Block> block : ModBlocks.BLOCK_CUTOUT)
            ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout());
        for (RegistryObject<Block> block : ModBlocks.BLOCK_TRANSLUCENT)
            ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.translucent());
    }

    public static <T> T log(T message) {
        LOGGER.info(String.valueOf(message));
        return message;
    }
}